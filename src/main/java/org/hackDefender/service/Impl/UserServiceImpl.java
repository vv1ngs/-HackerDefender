package org.hackDefender.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.Const;
import org.hackDefender.common.ResponseCode;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.ChallengeMapper;
import org.hackDefender.dao.ContainerMapper;
import org.hackDefender.dao.UserMapper;
import org.hackDefender.pojo.User;
import org.hackDefender.service.UserService;
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
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ChallengeMapper challengeMapper;

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


    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setGoldenCount(0);
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNoneBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    public ServerResponse<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorCode(ResponseCode.NEED_ADD_CONTAINER.getCode(), "无该用户，请重新登录");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        if (user.getEmail() != null) {
            int resultCount = userMapper.checkUsername(user.getEmail());
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("email以存在");
            }
        }
        if (user.getPhone() != null) {
            int resultCount = userMapper.checkPhone(user.getPhone());
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("email以存在");
            }
        }
        User updateuser = new User();
        updateuser.setId(user.getId());
        updateuser.setEmail(user.getEmail());
        updateuser.setPhone(user.getPhone());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateuser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateuser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }


    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("更新密码失败");
    }
}
