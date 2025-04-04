package com.apps.common.controller;

import com.apps.common.ResponseResult;
import com.apps.common.model.dto.LoginRequest;
import com.apps.common.model.dto.RefreshTokenRequest;
import com.apps.common.model.dto.TokenResponse;
import com.apps.common.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 获取Token
     */
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseResult<TokenResponse> getToken(@Validated @RequestBody LoginRequest request) {
        Map<String, String> map = authService.generateToken(request);
        TokenResponse response = new TokenResponse();
        response.setToken(map.get("token"));
        response.setRefreshToken(map.get("refreshToken"));
        response.setExpiresIn(Long.parseLong(map.get("expiresIn")));
        response.setRefreshExpiresIn(Long.parseLong(map.get("refreshExpiresIn")));
        return ResponseResult.success(response);
    }

    /**
     * 刷新Token
     */
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseResult<TokenResponse> refreshToken(@Validated @RequestBody RefreshTokenRequest request) {
        Map<String, String> map = authService.refreshToken(request);
        TokenResponse response = new TokenResponse();
        response.setToken(map.get("token"));
        response.setRefreshToken(map.get("refreshToken"));
        response.setExpiresIn(Long.parseLong(map.get("expiresIn")));
        response.setRefreshExpiresIn(Long.parseLong(map.get("refreshExpiresIn")));
        return ResponseResult.success(response);
    }
} 