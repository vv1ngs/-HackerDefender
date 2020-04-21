package org.hackDefender.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.User;
import org.hackDefender.service.UserService;
import org.hackDefender.util.Constant;
import org.hackDefender.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vvings
 * @version 2020/4/13 23:42
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 传入用户名，检测数据库中是否有用户使用该用户名
     * 主要用于注册前检测
     *
     * @param username 用户名
     * @return
     */
    @Override
    public ServerResponse<String> checkUsername(String username) {

        ServerResponse<String> responseMessage = null;

        //查找出全部该用户名用户
        int i = userMapper.checkUsername(username);

        if (i >= 1){
            //存在返回
            responseMessage = ServerResponse.createByErrorMessage(Constant.USERNAME_EXIST);
        }else {
            //不存在返回
            responseMessage = ServerResponse.createBySuccess();
        }

        return responseMessage;
    }

    /**
     * 传入一个 org.hackDefender.pojo.User 对象，添加到数据库
     * 主要用于注册
     *
     * @param user org.hackDefender.pojo.User实例
     * @return
     */
    @Override
    public ServerResponse<String> insertUser(User user) {

        ServerResponse<String> responseMessage = null;

        if (user != null){
            //插入成功返回
            int result = userMapper.insert(user);
            responseMessage = ServerResponse.createBySuccess(Constant.REGISTER_SUCCESS);
        }else {
            //失败返回
            responseMessage = ServerResponse.createByErrorMessage(Constant.REGISTER_FAILED) ;
        }

        return responseMessage;

    }

    /**
     * 根据用户名和用户密码查找存在用户
     * 主要用于登录
     *
     * @param username  用户名
     * @param password  用户密码
     * @return
     */
    @Override
    public ServerResponse<User> selectUser(String username, String password) {

        ServerResponse<User> responseMessage = null;

        if (username != null && password != null){

            String MD5Password = MD5Util.MD5EncodeUtf8(password);

            User user = userMapper.selectLogin(username, MD5Password);


            if (user != null){
                //清空密码
                user.setPassword(StringUtils.EMPTY);

                //登录成功
                responseMessage = ServerResponse.createBySuccess(Constant.LOGIN_SUCCESS,user);

            }else {

                responseMessage = ServerResponse.createByErrorMessage(Constant.LOGIN_FAILED);
            }
        }else {

            responseMessage = ServerResponse.createByErrorMessage(Constant.LOGIN_FAILED);
        }


        return responseMessage;
    }

    /**
     * 更新用户信息，并返回更新后的 user 对象
     *
     * @param user 已更改好信息的 user
     * @return
     */
    @Override
    public User updateUserInfo(User user) {

        User newUser = null;

        //更新用户信息，不包括密码
        int result = userMapper.updateByPrimaryKey(user);

        if (result >= 1){
            //重新获得该用户信息
            newUser = userMapper.selectByPrimaryKey(user.getId());

            newUser.setPassword(null);
        }
        return newUser;
    }


}
