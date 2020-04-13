package org.hackDefender.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.User;
import org.hackDefender.service.UserService;
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

    @Override
    public ServerResponse<User> login(String username, String password) {
        if (username == null || password == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //String MD5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }
}
