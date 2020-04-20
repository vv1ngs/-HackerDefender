package org.hackDefender.controller.portal;



import org.hackDefender.common.Const;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;
import org.hackDefender.service.Impl.UserServiceImpl;
import org.hackDefender.util.Constant;
import org.hackDefender.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;


/**
 * @author Playwi0
 * @version 2020/4/15 10:15
 *
 * 用户数据操作
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    /**
     * 检查用户名是否存在
     *
     * @param username 传入参数用户名
     * @return
     */
    @RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkUsername(@RequestParam("username") String username){

        ServerResponse<String> responseMessage = null;

        if (username != null) {

            responseMessage = userService.checkUsername(username);
        }

        return responseMessage;
    }

    /**
     * 用户注册
     *
     * @param username  用户名
     * @param pwd       用户密码
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestParam("username") String username,
                                           @RequestParam("pwd") String pwd){

        ServerResponse<String> ResponseMessage = null;

        //先检查用户名是否存在
        ServerResponse checkResult = userService.checkUsername(username);

        if (checkResult.isSuccess()){

            //设置用户用户名
            User user = new User();
            user.setUsername(username);

            //设置用户密码，密码是MD5加密后的密码
            String MD5Pwd = MD5Util.MD5EncodeUtf8(pwd);
            user.setPassword(MD5Pwd);

            //角色权限
            user.setRole(1);

            //设置初始金币数量
            user.setGoldenCount(0);

            //设置用户创建时间
            Date date = new Date();
            user.setCreateTime(date);

            //往数据库添加该user
            ResponseMessage = userService.insertUser(user);

        }else {
            //失败返回
            ResponseMessage = ServerResponse.createByErrorMessage(Constant.USERNAME_EXIST);
        }

        return ResponseMessage;
    }


    /**
     * 用户登录
     *
     * @param username  用户名
     * @param pwd       用户密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestParam("username") String username,
                                      @RequestParam("pwd") String pwd, HttpSession session){

        ServerResponse<User> responseMessage = null;

        if (username != null && pwd != null){

            ServerResponse<User> response = userService.selectUser(username, pwd);

            if (response.isSuccess()){

                session.setAttribute(Const.CURRENT_USER, response.getData());

                responseMessage = response;

            }else {

                responseMessage = response;
            }

        }else {

           responseMessage = ServerResponse.createByErrorMessage(Constant.LOGIN_FAILED);
        }

        return responseMessage;
    }


}
