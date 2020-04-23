package org.hackDefender.service;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.vo.ContainerVo;

/**
 * @author vvings
 * @version 2020/4/20 22:52
 */
public interface ChallengeService {


    ServerResponse<ContainerVo> getChallenge(Integer challengeId, Integer userId);
}
