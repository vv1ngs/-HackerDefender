package org.hackDefender.service.Impl;

import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.ChallengeMapper;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.Container;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ChallengeService;
import org.hackDefender.vo.ContainerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author vvings
 * @version 2020/4/20 22:53
 */
@Service("challengeService")
public class ChallengeServiceImpl implements ChallengeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ChallengeMapper challengeMapper;


    @Override
    public ServerResponse<ContainerVo> getChallenge(Integer challengeId, Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "没有该用户");
        }
        Container container = containerMapper.selectByUid(userId);
        if (container == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_ADD_CONTAINER.getCode(), "还没创建容器");
        }
        ContainerVo containerVo = assembleOrderItemVo(container);
        return ServerResponse.createBySuccess(containerVo);
    }

    private ContainerVo assembleOrderItemVo(Container container) {
        long time = new Date().getTime() - container.getCreateTime().getTime();
        String ip = String.valueOf(container.getUserId()) + "-" + container.getUuid();
        ContainerVo challengeVo = new ContainerVo();
        challengeVo.setPort(String.valueOf(container.getPort()));
        challengeVo.setRemainTime(time);
        challengeVo.setIp(ip);
        return challengeVo;
    }
}
