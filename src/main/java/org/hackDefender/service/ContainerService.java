package org.hackDefender.service;

import com.github.pagehelper.PageInfo;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.vo.ContainerVo;

/**
 * @author vvings
 * @version 2020/4/21 11:02
 */
public interface ContainerService {
    ServerResponse AutoCloseContainer();

    ServerResponse addContainer(Integer challengeId, Integer userId);

    ServerResponse<PageInfo> getContainerList(int pageNum, int pageSize);

    ServerResponse lengthContainer(Integer challengeId, Integer userId);

    ServerResponse<ContainerVo> removeContainer(Integer challengeId, Integer userId);
}