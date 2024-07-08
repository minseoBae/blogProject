package com.example.blogproject.service;

import com.example.blogproject.entity.Role;
import com.example.blogproject.entity.User;
import com.example.blogproject.repository.RoleRepository;
import com.example.blogproject.repository.UserRepository;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User registerUser(User user){
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
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
}
