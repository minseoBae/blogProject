package com.example.blogproject.service.user;

import com.example.blogproject.entity.user.Role;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.RoleRepository;
import com.example.blogproject.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenizer jwtTokenizer;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void registerUser(User user){
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Boolean authenticateUser(String username, String password) {
        User loginUser = userRepository.findByUsername(username);

        return (loginUser != null && bCryptPasswordEncoder.matches(password, loginUser.getPassword()));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(HttpServletRequest request) {
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
        if (token != null) {
            Long userId = jwtTokenizer.getUserIdFromToken(token);
            return userRepository.findById(userId).orElse(null);
        } else
            return null;
    }
}
