package org.hackDefender.service;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.pojo.User;

/**
 * @author vvings
 * @version 2020/4/13 23:42
 */
public interface UserService {
    ServerResponse<User> login(String username, String password);
}
