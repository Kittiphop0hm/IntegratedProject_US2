package com.example.backend.controllers.users;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.RequestLoginDto;
import com.example.backend.dtos.users.ResponseTokenDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.services.users.JwtService;
import com.example.backend.services.users.UserFileService;
import com.example.backend.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v2/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins}", allowCredentials = "true")
public class UserAuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> registerUser(@ModelAttribute RegisterFormDto userForm, @RequestParam(required = false) MultipartFile cardFrontImage, @RequestParam(required = false) MultipartFile cardBackImage) {
        return ResponseEntity.status(201).body(userService.createUser(userForm, cardFrontImage, cardBackImage));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseUserDto> verifyEmail(@RequestParam String token) {
        Integer userId = jwtService.extractUserId(token);
        String email = jwtService.extractEmail(token);
        ResponseUserDto user = userService.verifyEmail(userId, email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseTokenDto> authenticateUser(
            @Valid @RequestBody RequestLoginDto requestLoginDto, HttpServletResponse response) {
        System.out.println("authentication called");
        return ResponseEntity.ok(userService.checkLogin(
                requestLoginDto.getEmail(),
                requestLoginDto.getPassword(),
                response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseTokenDto> refreshAccessToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        return ResponseEntity.ok(userService.refreshAccessToken(refreshToken));
    }
}
