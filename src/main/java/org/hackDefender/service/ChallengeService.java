package org.hackDefender.service;

import com.github.pagehelper.PageInfo;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.vo.ChallengeVo;
import org.hackDefender.vo.ContainerVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author vvings
 * @version 2020/4/20 22:52
 */
public interface ChallengeService {


    ServerResponse<ContainerVo> getContainer(Integer userId);

    ServerResponse uploadScript(MultipartFile file, Integer challengId, String path, Integer status);

    ServerResponse saveOrUpdateChallenge(Challenge challenge);

    ServerResponse<PageInfo> listChallenge(int pageNum, int pageSize);

    ServerResponse attack(Integer useId);

    ServerResponse<String> catLogs(Integer id);

    ServerResponse check(Integer id);

    ServerResponse userUpload(Integer id, MultipartFile file, String localPath);

    ServerResponse<ChallengeVo> getChallenge(Integer challengeId);
}
