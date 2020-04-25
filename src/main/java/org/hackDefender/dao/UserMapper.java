package org.hackDefender.dao;

import org.apache.ibatis.annotations.Param;
import org.hackDefender.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    int checkEmail(String email);

    int checkPhoneByUserId(@Param("phone") String phone, @Param("id") Integer id);

    int checkPassword(@Param("md5password") String md5password, @Param("id") Integer id);

    int checkEmailByUserId(@Param("email") String email, @Param("id") Integer id);
}