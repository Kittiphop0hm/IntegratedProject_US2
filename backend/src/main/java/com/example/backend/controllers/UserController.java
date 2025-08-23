package com.example.backend.controllers;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseUserDto> registerUser(@RequestBody RegisterFormDto userForm) {
        return ResponseEntity.status(201).body(userService.createUser(userForm));
    }
}
