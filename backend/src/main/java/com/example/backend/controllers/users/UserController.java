package com.example.backend.controllers.users;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.dtos.users.ResponseUserPictureDto;
import com.example.backend.services.users.UserFileService;
import com.example.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v2/register")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFileService userFileService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ResponseUserPictureDto>> findUserPictureByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userFileService.findPictureByUserId(userId));
    }

    @GetMapping("")
    public ResponseEntity<ResponseUserDto> findUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping("")
    public ResponseEntity<ResponseUserDto> registerUser(@ModelAttribute RegisterFormDto userForm, @RequestParam(required = false) MultipartFile cardFrontImage, @RequestParam(required = false) MultipartFile cardBackImage) {
        return ResponseEntity.status(201).body(userService.createUser(userForm, cardFrontImage, cardBackImage));
    }
}
