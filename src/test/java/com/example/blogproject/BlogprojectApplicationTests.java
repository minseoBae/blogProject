package com.example.blogproject;

import com.example.blogproject.entity.User;
import com.example.blogproject.repository.UserRepository;
import com.example.blogproject.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogprojectApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("패스워드 암호화 테스트")
    void passwordEncode() {
        // given
        String username = "test2";
        String rawPassword = "123456789";

        User user = new User();
        user.setUsername(username);
        user.setName("test2");
        user.setEmail("test2@gmail.com");
        user.setPassword(rawPassword);

        // when
        userService.registerUser(user);
        String encodedPassword = userRepository.findByUsername(username).getPassword();

        // then
        assertAll(
                () -> assertNotEquals(rawPassword, encodedPassword),
                () -> assertTrue(bCryptPasswordEncoder.matches(rawPassword, encodedPassword))
        );
    }

}
