package com.apps.common.security;

import com.apps.common.exception.AuthException;
import com.apps.common.exception.ErrorCode;
import com.apps.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于拦截请求并验证JWT令牌
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 获取Authorization请求头
            final String authHeader = request.getHeader("Authorization");
            
            // 如果请求头为空或不是Bearer类型，直接放行
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 提取token
            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.extractUsername(jwt);

            // 如果用户名不为空且当前没有认证信息，则进行认证
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                // 验证token
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new AuthException(ErrorCode.TOKEN_INVALID);
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            // 对于认证异常，直接抛出
            throw e;
        } catch (Exception e) {
            // 对于其他异常，转换为认证异常
            throw new AuthException(ErrorCode.SYSTEM_ERROR, "JWT认证过程中发生异常：" + e.getMessage());
        }
    }
} 