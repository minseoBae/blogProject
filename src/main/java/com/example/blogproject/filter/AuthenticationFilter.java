package com.example.blogproject.filter;

import com.example.blogproject.entity.user.Role;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.RoleRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {
    private final JwtTokenizer jwtTokenizer;
    private final RoleRepository roleRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String refreshToken = null;

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("refreshToken")) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (refreshToken != null) {
                Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);

                Long userId = claims.get("userId", Long.class);
                String username = claims.get("username", String.class);
                String name = claims.get("name", String.class);
                String email = claims.getSubject();
                List<String> roleNames = claims.get("roles", List.class);

                User user = new User();
                user.setId(userId);
                user.setUsername(username);
                user.setName(name);
                user.setEmail(email);
                Set<Role> roles = roleRepository.findByNameIn(roleNames);
                user.setRoles(roles);

                // Save user information to ThreadLocal
                UserContextHolder.setUser(user);
            }

            // Pass the request to the next filter or servlet
            chain.doFilter(request, servletResponse);
        } finally {
            UserContextHolder.clear();
        }
    }
}