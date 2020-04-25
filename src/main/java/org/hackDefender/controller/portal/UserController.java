package org.hackDefender.controller.portal;


import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;
import org.hackDefender.service.Impl.UserServiceImpl;
import org.hackDefender.util.CookieUtil;
import org.hackDefender.util.JacksonUtil;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author vvings
 * @version 2020/4/13 23:38
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServerResponse) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            CookieUtil.writeLoginToken(httpServerResponse, session.getId());
            RedisPoolSharedUtil.setEx(session.getId(), JacksonUtil.ObjToString(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.deleteToken(httpServletResponse, httpServletRequest);
        RedisPoolSharedUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "update_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpServletRequest httpServletRequest, User user) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User currentUser = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        user.setUsername(currentUser.getUsername());
        user.setId(currentUser.getId());
        ServerResponse<User> response = userService.updateUserInfo(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            RedisPoolSharedUtil.setEx(loginToken, JacksonUtil.ObjToString(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordNew, String passwordOld) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(user, passwordOld, passwordNew);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String userJsonStr = RedisPoolSharedUtil.get(loginToken);
        User user = JacksonUtil.String2ToObj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return userService.getInformation(user.getId());
    }
}
