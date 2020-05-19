package org.hackDefender.controller.portal;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ChallengeService;
import org.hackDefender.service.ContainerService;
import org.hackDefender.util.CookieUtil;
import org.hackDefender.util.JacksonUtil;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.hackDefender.vo.ContainerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author vvings
 * @version 2020/4/20 22:52
 */
@Controller
@RequestMapping("/user")
public class ChallengeController {
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ContainerService containerService;


    @RequestMapping(value = "add_container.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> addContainer(Integer challengeId, HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(user);
        }
        if (CookieUtil.frequency_limit(user.getUuid())) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return containerService.addContainer(challengeId, user.getId());
    }

    @RequestMapping(value = "get_container.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ContainerVo> getContainer(Integer challengeId, HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        return challengeService.getChallenge(challengeId, user.getId());
    }

    @RequestMapping(value = "length_container.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ContainerVo> lengthContainer(Integer challengeId, HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (CookieUtil.frequency_limit(user.getUuid())) {
            return ServerResponse.createByErrorMessage("请求过于频繁");
        }
        return containerService.lengthContainer(user.getId());
    }

    @RequestMapping(value = "del_container.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ContainerVo> deleteContainer(HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (CookieUtil.frequency_limit(user.getUuid())) {
            return ServerResponse.createByErrorMessage("请求过于频繁");
        }
        return containerService.removeContainer(user.getId());
    }

    @RequestMapping(value = "list_challenge.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listChallenge(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pagerSize", defaultValue = "10") int pagerSize) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (CookieUtil.frequency_limit(user.getUuid())) {
            return ServerResponse.createByErrorMessage("请求过于频繁");
        }
        return challengeService.listChallenge(pageNum, pagerSize);
    }

    @RequestMapping(value = "attack.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse attack(HttpServletRequest httpServletRequest, Integer challengeId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (CookieUtil.frequency_limit(user.getUuid())) {
            return ServerResponse.createByErrorMessage("请求过于频繁");
        }
        return challengeService.attack(user.getId(), challengeId);
    }
}
