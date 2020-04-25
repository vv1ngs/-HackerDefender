package org.hackDefender.controller.backend;

import com.github.pagehelper.PageInfo;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author vvings
 * @version 2020/4/21 16:22
 */
@RequestMapping("/manage/challenge")
@Controller
public class ManagerChallengeController {
    @Autowired
    private ContainerService containerService;

    @ResponseBody
    @RequestMapping("/list_container.do")
    public ServerResponse<PageInfo> getContainerList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pagerSize", defaultValue = "10") int pagerSize) {
        return containerService.getContainerList(pageNum, pagerSize);
    }
}
