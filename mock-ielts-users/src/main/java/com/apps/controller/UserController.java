package com.apps.controller;

import com.apps.common.ResponseResult;
import com.apps.model.User;
import com.apps.service.UserService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 获取日志记录器
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // 注册用户
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<String>> registerUser(@RequestBody User user) {
        logger.debug("注册用户：" + user.getUsername());
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(ResponseResult.success("注册成功",200,null));
        } catch (Exception e) {
            System.out.println("注册失败：" + user);
            logger.error("注册失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.fail("注册失败：" + e.getMessage()));
        }
    }

    // 登录用户
//    @PostMapping("/login")
//    public ResponseEntity<ResponseResult<String>> loginUser(@RequestBody User user) {
//        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
//        if (loggedInUser != null) {
//            HashMap<String, String> respon = new HashMap<String, String>();
//            respon.put("role",loggedInUser.getRole());
//            respon.put("userid",String.valueOf(loggedInUser.getId()));
//            return ResponseEntity.ok(ResponseResult.success("登录成功",200,respon.toString()));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(ResponseResult.fail("用户名或密码错误"));
//        }
//    }
    @PostMapping("/login")
    public ResponseResult<Map<String, String>> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            Map<String, String> respon = new HashMap<String, String>();
            respon.put("role",loggedInUser.getRole());
            respon.put("userId",String.valueOf(loggedInUser.getId()));
            respon.put("username",loggedInUser.getUsername());
            return  ResponseResult.success("登录成功",200,respon);
        } else {
            return  ResponseResult.fail("用户名或密码错误");
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseResult<String>> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(ResponseResult.success("User deleted successfully", 200, null));
        } catch (Exception e) {
            logger.error("删除用户失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.fail("Failed to delete user: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password/{userId}")
    public ResponseEntity<ResponseResult<String>> resetPassword(@PathVariable Long userId) {
        try {
            userService.resetPassword(userId);
            return ResponseEntity.ok(ResponseResult.success("Password reset successfully", 200, null));
        } catch (Exception e) {
            logger.error("更新密码失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.fail("Failed to reset password: " + e.getMessage()));
        }
    }

}