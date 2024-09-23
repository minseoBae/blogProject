package com.example.blogproject.service.user;

import com.example.blogproject.dto.UserSaveRequestDTO;
import com.example.blogproject.entity.user.Role;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.RoleRepository;
import com.example.blogproject.repository.user.UserRepository;
import com.example.blogproject.service.image.FileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenizer jwtTokenizer;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileService fileService;

    @Transactional
    public void registerUser(UserSaveRequestDTO userSaveRequestDTO) throws IOException {
        String uploadDir = "C://Temp/upload/profile-images";
        String uploadedFileUrl = "/upload/profile-images/"+ fileService.storeFile(userSaveRequestDTO.getProfileImage(), uploadDir);
        User newUser = new User();
        newUser.setUsername(userSaveRequestDTO.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(userSaveRequestDTO.getPassword()));
        newUser.setEmail(userSaveRequestDTO.getEmail());
        newUser.setName(userSaveRequestDTO.getName());
        newUser.setProfileImage(uploadedFileUrl);
        newUser.setBlogName(userSaveRequestDTO.getUsername() + ".log");

        Role userRole = roleRepository.findByName("ROLE_USER");
        newUser.setRoles(Collections.singleton(userRole));

        userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Boolean authenticateUser(String username, String password) {
        User loginUser = userRepository.findByUsername(username);

        return (loginUser != null && bCryptPasswordEncoder.matches(password, loginUser.getPassword()));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByName(String username) {
        return userRepository.existsByUsername(username);
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

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
