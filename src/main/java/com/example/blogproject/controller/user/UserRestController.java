package com.example.blogproject.controller.user;

import com.example.blogproject.dto.UserLoginResponse;
import com.example.blogproject.entity.user.RefreshToken;
import com.example.blogproject.entity.user.Role;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.user.UserRepository;
import com.example.blogproject.security.dto.UserLoginDto;
import com.example.blogproject.service.user.RefreshTokenService;
import com.example.blogproject.service.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController {
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        boolean exists = userService.existsByName(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto,
                                   BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

        User loginUser = userService.findByUsername(userLoginDto.getUsername());

        if (!userService.authenticateUser(userLoginDto.getUsername(), userLoginDto.getPassword())) {
            //비밀번호가 일치하지 않을 경우
            return new ResponseEntity("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
        } else {
            List<String> roles = loginUser.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            String accessToken = jwtTokenizer.createAccessToken(
                    loginUser.getId(), loginUser.getEmail(), loginUser.getName(), loginUser.getUsername(), roles);
            String refreshToken = jwtTokenizer.createRefreshToken(
                    loginUser.getId(), loginUser.getEmail(), loginUser.getName(), loginUser.getUsername(), roles);

            //refreshToken을 DB에 저장
            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setValue(refreshToken);
            refreshTokenEntity.setUserId(loginUser.getId());

            refreshTokenService.addRefreshToken(refreshTokenEntity);

            //응답으로 보낼 값 준비
            UserLoginResponse loginResponseDto = UserLoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userId(loginUser.getId())
                    .name(loginUser.getName())
                    .build();

            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(true); //보안(쿠키 값을 자바 스크립트 같은 곳에서는 접근할 수 없다.)
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000)); //쿠키의 유지시간 단위는 초, jwt 토큰의 유지시간 단위는 밀리세컨드

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT / 1000));

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            return new ResponseEntity(loginResponseDto, HttpStatus.OK);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if(refreshToken == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        Long userId = Long.valueOf((Integer)claims.get("userId"));

        User user = userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다."));

        List roles = (List)claims.get("roles");

        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getName(), user.getUsername(), roles);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(jwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        response.addCookie(accessTokenCookie);
        UserLoginResponse responseDto = UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .userId(user.getId())
                .build();

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
