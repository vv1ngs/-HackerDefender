package org.hackDefender.service;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;

/**
 * @author vvings
 * @version 2020/4/13 23:42
 */
public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<User> getInformation(Integer id);

    ServerResponse<String> register(User user);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew);

    ServerResponse openShell(Integer userId);
}
