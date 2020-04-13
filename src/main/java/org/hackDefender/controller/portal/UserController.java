package org.hackDefender.controller.portal;


import org.hackDefender.common.Const;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;
import org.hackDefender.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author vvings
 * @version 2020/4/13 23:38
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }
}
