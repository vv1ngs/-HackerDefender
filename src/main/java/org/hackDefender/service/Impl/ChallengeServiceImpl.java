package org.hackDefender.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.ChallengeMapper;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.pojo.Container;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ChallengeService;
import org.hackDefender.util.FtpUtil;
import org.hackDefender.util.UUIDUtil;
import org.hackDefender.vo.ContainerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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

    @Override
    public ServerResponse uploadScript(MultipartFile file, Integer challengId, String path) {
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
                    return ServerResponse.createBySuccess("更新题目成功");
                }
                return ServerResponse.createByErrorMessage("更新题目失败");
            } else {
                int rowCount = challengeMapper.insertSelective(challenge);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增题目成功");
                }
                return ServerResponse.createByErrorMessage("新增题目失败");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");

    }
}
