// UserService.java
package com.apps.service;

import com.apps.mapper.RoleMapper;
import com.apps.mapper.UserMapper;
import com.apps.model.Role;
import com.apps.model.User;
 import org.springframework.beans.factory.annotation.Autowired;
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
}