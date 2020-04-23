package org.hackDefender.controller.backend;

import org.hackDefender.common.Const;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;
import org.hackDefender.service.ManagerService;
import org.hackDefender.service.UserService;
import org.hackDefender.util.CookieUtil;
import org.hackDefender.util.JacksonUtil;
import org.hackDefender.util.RedisPoolSharedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author vvings
 * @version 2020/4/21 10:48
 */
@Controller
@RequestMapping("/manager")
public class ManagerUserController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                RedisPoolSharedUtil.setEx(session.getId(), JacksonUtil.ObjToString(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }

        }
        return response;
    }
    
}
