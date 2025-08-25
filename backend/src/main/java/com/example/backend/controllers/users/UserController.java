package com.example.backend.controllers.users;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.dtos.users.ResponseUserPictureDto;
import com.example.backend.services.users.JwtService;
import com.example.backend.services.users.UserFileService;
import com.example.backend.services.users.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFileService userFileService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ResponseUserPictureDto>> findUserPictureByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userFileService.findPictureByUserId(userId));
    }

    @GetMapping("")
    public ResponseEntity<ResponseUserDto> findUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

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
}
