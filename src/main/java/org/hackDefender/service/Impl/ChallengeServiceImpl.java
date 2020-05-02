package org.hackDefender.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.CategoryMapper;
import org.hackDefender.dao.ChallengeMapper;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.Category;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.pojo.Container;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ChallengeService;
import org.hackDefender.util.FtpUtil;
import org.hackDefender.util.UUIDUtil;
import org.hackDefender.vo.ChallengeVo;
import org.hackDefender.vo.ContainerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vvings
 * @version 2020/4/20 22:53
 */
@Slf4j
@Service("challengeService")
public class ChallengeServiceImpl implements ChallengeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ChallengeMapper challengeMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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
        ContainerVo containerVo = assembleContainVo(container);
        return ServerResponse.createBySuccess(containerVo);
    }

    private ContainerVo assembleContainVo(Container container) {
        long time = new Date().getTime() - container.getCreateTime().getTime();
        String ip = String.valueOf(container.getUserId()) + "-" + container.getUuid();
        ContainerVo containerVo = new ContainerVo();
        containerVo.setPort(String.valueOf(container.getPort()));
        containerVo.setRemainTime(time);
        containerVo.setIp(ip);
        return containerVo;
    }

    private ChallengeVo assembleChallengeItemVo(Challenge challenge) {
        ChallengeVo challengeVo = new ChallengeVo();
        challengeVo.setDetail(challenge.getDetail());
        challengeVo.setGolden(challenge.getGolden());
        challengeVo.setId(challenge.getId());
        Category category = categoryMapper.selectByPrimaryKey(challenge.getCategoryId());
        challengeVo.setCategoryName(category.getName());
        if (category.getParentId() != 0) {
            Category categoryparent = categoryMapper.selectByPrimaryKey(category.getParentId());
            challengeVo.setTopCategoryName(categoryparent.getName());
        } else challengeVo.setTopCategoryName("null");
        return challengeVo;
    }

    @Override
    public ServerResponse uploadScript(MultipartFile file, Integer challengId, String path) {
        if (file == null) {
            return ServerResponse.createByErrorMessage("错误");
        }
        String fileName = file.getOriginalFilename();
        String filExtionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUIDUtil.getUUID8() + "." + filExtionName;
        log.info("上传文件{},路径{}", uploadFileName, path);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
            Map map = Maps.newHashMap();
            map.put("uri", targetFile.getName());
            map.put("url", "http://118.24.120.71" + targetFile.getName());
            Challenge challenge = new Challenge();
            challenge.setId(challengId);
            challenge.setScriptUrl(targetFile.getName());
            challengeMapper.updateByPrimaryKeySelective(challenge);
            return ServerResponse.createBySuccess("上传成功", map);
        } catch (IOException e) {
            log.error("上传文件异常");
            return ServerResponse.createByErrorMessage("上传失败");
        }

    }

    @Override
    public ServerResponse saveOrUpdateChallenge(Challenge challenge) {
        if (challenge != null) {
            if (challenge.getId() != null) {
                int rowCount = challengeMapper.updateByPrimaryKeySelective(challenge);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新题目成功", challenge);
                }
                return ServerResponse.createByErrorMessage("更新题目失败");
            } else {
                int rowCount = challengeMapper.insert(challenge);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增题目成功", challenge);
                }
                return ServerResponse.createByErrorMessage("新增题目失败");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");

    }

    @Override
    public ServerResponse<PageInfo> listChallenge(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Challenge> lists = challengeMapper.selectAll();
        List<ChallengeVo> list2 = lists.stream().map(item -> {
            ChallengeVo vo = assembleChallengeItemVo(item);
            return vo;
        }).collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo(list2);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
