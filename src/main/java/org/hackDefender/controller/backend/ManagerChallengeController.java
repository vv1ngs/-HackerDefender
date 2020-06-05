package org.hackDefender.controller.backend;

import com.github.pagehelper.PageInfo;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.interceptor.Permission;
import org.hackDefender.interceptor.RequestLogin;
import org.hackDefender.pojo.Challenge;
import org.hackDefender.service.ChallengeService;
import org.hackDefender.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author vvings
 * @version 2020/4/21 16:22
 */
@RequestMapping("/manage/challenge")
@Controller
@RequestLogin(desc = Permission.REQUEST_ADMIN)
public class ManagerChallengeController {
    @Autowired
    private ContainerService containerService;
    @Autowired
    private ChallengeService challengeService;

    @ResponseBody
    @RequestMapping(value = "/list_container.do", method = RequestMethod.GET)
    public ServerResponse<PageInfo> getContainerList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pagerSize", defaultValue = "10") int pagerSize) {
        return containerService.getContainerList(pageNum, pagerSize);
    }


    @ResponseBody
    @RequestMapping(value = "/upload_script.do", method = RequestMethod.POST)
    public ServerResponse uploadScript(@RequestParam(value = "script_file") MultipartFile file, Integer challengeId, HttpServletRequest req) {
        String path = req.getSession().getServletContext().getRealPath("upload");
        return challengeService.uploadScript(file, challengeId, path);
    }


    @ResponseBody
    @RequestMapping(value = "/add_challenge.do", method = RequestMethod.POST)
    public ServerResponse<Challenge> addorUpdateChallenge(Challenge challenge) {
        return challengeService.saveOrUpdateChallenge(challenge);
    }
}
