package org.hackDefender.service;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;

/**
 * @author vvings
 * @version 2020/4/13 23:42
 */
public interface UserService {

    ServerResponse checkUsername(String username);

    ServerResponse<String> insertUser(User user);

    ServerResponse<User> selectUser(String username, String password);

    User updateUserInfo(User user);
}
