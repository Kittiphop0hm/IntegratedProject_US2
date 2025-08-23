package com.example.backend.controllers.users;

import com.example.backend.services.users.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userPicture")
public class UserFileController {
    @Autowired
    private UserFileService service;

}
