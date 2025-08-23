package com.example.backend.controllers.users;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/register")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseUserDto> registerUser(@ModelAttribute RegisterFormDto userForm, @RequestParam List<MultipartFile> files) {
        return ResponseEntity.status(201).body(userService.createUser(userForm, files));
    }
}
