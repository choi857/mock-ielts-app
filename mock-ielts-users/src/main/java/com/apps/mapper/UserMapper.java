package com.apps.mapper;

import com.apps.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User selectUserById(Long id);

    User selectUserByUsername(String username);

    void updateUser(User user);

    void deleteUserById(Long id);
    List<User> selectAllUsers();
 }