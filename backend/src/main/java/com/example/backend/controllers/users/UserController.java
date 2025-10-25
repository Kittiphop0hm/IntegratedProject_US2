package com.example.backend.controllers.users;

import com.example.backend.dtos.users.*;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.services.users.JwtService;
import com.example.backend.services.users.UserFileService;
import com.example.backend.services.users.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin(origins = "${app.cors.allowed-origins}", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFileService userFileService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/picture/{userId}")
    public ResponseEntity<List<ResponseUserPictureDto>> findUserPictureByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userFileService.findPictureByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> findUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("")
    public ResponseEntity<ResponseUserDto> findUserByEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(@PathVariable Integer id, @RequestBody UserUpdateFormatDto userFormat) {
        return ResponseEntity.ok(userService.updateUser(id, userFormat));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordDto changePasswordDto,
            Authentication authentication) {

        AuthUserDetail userDetails = (AuthUserDetail) authentication.getPrincipal();
        userService.changePassword(id, userDetails.getId(), changePasswordDto);
        return ResponseEntity.noContent().build();
    }

}
