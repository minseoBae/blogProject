package com.example.blogproject.service;

import com.example.blogproject.entity.RefreshToken;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LogoutService implements LogoutHandler {
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        Long userId = jwtTokenizer.getUserIdFromToken(token);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);

        //로그아웃이기 때문에 만료시키고 폐지시킨다.
        if (refreshToken != null) {
            refreshToken.setExpired(true);
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            log.info("Refresh token invalidated for user ID: {}", userId);
        } else {
            log.warn("No refresh token found for user ID: {}", userId);
        }
    }
}
