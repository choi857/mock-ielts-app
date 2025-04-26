// UserService.java
package com.apps.service;

import com.apps.mapper.RoleMapper;
import com.apps.mapper.UserMapper;
import com.apps.model.Role;
import com.apps.model.User;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    @Async("UserExecutor")
    public void registerUser(User user) {

        User existingUser = userMapper.selectUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 加密密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // 设置角色
        Role role = new Role();
        String userRole =   String.valueOf(Optional.ofNullable(user.getRole()).orElse("user")) ;

        role.setDescription("用户描述，暂时不写");
        role.setRoleId(user.getId());
        role.setRoleName(userRole);
        roleMapper.insert(role);
        userMapper.insertUser(user);

    }

    public User loginUser(String username, String password) {
        User user = userMapper.selectUserByUsername(username);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void deleteUser(Long userId) {
        userMapper.deleteUserById(userId);
    }

    public void resetPassword(Long userId) {
        User user = userMapper.selectUserById(userId);
        if (user != null) {
            user.setPassword(bCryptPasswordEncoder.encode("Aa123456"));
            userMapper.updateUser(user);
        }
    }

    /**
     * 查询用户全部信息
     * @param userId
     * @return
     */
    public User getUserInfo(Long userId) {
        return userMapper.selectUserById(userId);
    }
    /**
     * 更新用户信息
     * @param user
     */
    public void updateUserInfo(User user) {

        // 检查是否提供了新的密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // 加密密码
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userMapper.updateUser(user);
    }


}