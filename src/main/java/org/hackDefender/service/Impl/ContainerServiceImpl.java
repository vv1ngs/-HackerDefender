package org.hackDefender.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateUtils;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.ChallengeMapper;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.pojo.Container;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ContainerService;
import org.hackDefender.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/4/21 11:02
 */
@Service("containerService")
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ChallengeMapper challengeMapper;


    public ServerResponse addContainer(Integer challengeId, Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "没有该用户");
        }
        int resultCount = challengeMapper.checkId(challengeId);
        if (resultCount <= 0) {
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "题目参数错误");
        }
        resultCount = containerMapper.checkUid(userId);
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("您已创建一个实例");
        }
        String uuid = UUIDUtil.getUUID8();
        Challenge challenge = challengeMapper.selectByPrimaryKey(challengeId);
        Map<String, String> map = DockerUtil.addContainer(userId, uuid, challenge.getPort(), challenge.getDockerImage(), challenge.getMemoryLimit(), challenge.getCupLimit());
        Container container = new Container();
        container.setChallengeId(challengeId);
        container.setPort(Integer.parseInt(map.get("containerPort")));
        container.setContainerId(map.get("ContainId"));
        container.setRenewCount(0);
        container.setStatus(true);
        container.setUserId(userId);
        container.setUuid(uuid);
        int rowCount = containerMapper.insert(container);
        if (rowCount == 1) {
            return ServerResponse.createBySuccess("创建实例成功", map);
        }
        return ServerResponse.createByErrorMessage("创建实例失败");
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 1, isolation = Isolation.DEFAULT)
    @Override
    public void AutoCloseContainer() {
        Date closeDate = DateUtils.addSeconds(new Date(), -Integer.parseInt(PropertiesUtil.getProperty("container_lasttime", "3600")));
        List<Container> containerList = containerMapper.selectByTime(DateTimeUtil.DateToString(closeDate));
        for (Container container : containerList) {
            //DockerUtil.removeContainer(container.getUserId(), container.getUuid());
            int port = container.getPort();
            if (port != 0) {
                RedisPoolSharedUtil.sAdd(port);
            }
            containerMapper.deleteByPrimaryKey(container.getId());
        }

    }

    @Override
    public void updateFrp() {
        List<Container> containerList = containerMapper.selectAll();
        List<List<String>> lists = Lists.newArrayList();
        for (Container container : containerList) {
            List<String> list = Lists.newArrayList();
            Challenge challenge = challengeMapper.selectByPrimaryKey(container.getChallengeId());
            list.add(container.getUserId() + "-" + container.getUuid());
            list.add(String.valueOf(challenge.getPort()));
            list.add(String.valueOf(container.getPort()));
            lists.add(list);
        }
        FrpUtil.rewriteFrp(lists);
    }

    @Override
    public ServerResponse removeContainer(Integer userId) {
        Container container = containerMapper.selectByUidAndCId(userId);
        if (container == null) {
            return ServerResponse.createByErrorMessage("您还未创建实例");
        }
        int port = container.getPort();
        DockerUtil.removeContainer(userId, container.getUuid());
        if (port != 0) {
            RedisPoolSharedUtil.sAdd(port);
        }
        containerMapper.deleteByPrimaryKey(container.getId());
        return ServerResponse.createBySuccess();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 1, isolation = Isolation.DEFAULT)
    @Override
    public ServerResponse lengthContainer(Integer userId) {
        int Count = containerMapper.checkUid(userId);
        if (Count == 0) {
            return ServerResponse.createByErrorMessage("还未创建实例");
        }
        Count = containerMapper.selectRenewCountById(userId);
        if (Count > Integer.parseInt(PropertiesUtil.getProperty("renew_count"))) {
            return ServerResponse.createByErrorMessage("已超过最大延长次数");
        }
        Container container = containerMapper.selectByUidAndCId(userId);
        Date date = new Date();
        container.setCreateTime(date);
        container.setUpdateTime(date);
        container.setRenewCount(container.getRenewCount() + 1);
        containerMapper.updateByPrimaryKeySelective(container);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<PageInfo> getContainerList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Container> containerList = containerMapper.selectAll();
        PageInfo pageResult = new PageInfo(containerList);
        return ServerResponse.createBySuccess(pageResult);
    }
}
