package org.hackDefender.service;

import com.github.pagehelper.PageInfo;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.vo.ContainerVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author vvings
 * @version 2020/4/20 22:52
 */
public interface ChallengeService {


    ServerResponse<ContainerVo> getChallenge(Integer challengeId, Integer userId);

    ServerResponse uploadScript(MultipartFile file, Integer challengId, String path);

    ServerResponse saveOrUpdateChallenge(Challenge challenge);

    ServerResponse<PageInfo> listChallenge(int pageNum, int pageSize);

    ServerResponse attack(Integer useId);
}
