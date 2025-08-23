package com.example.backend.services;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public ResponseUserDto createUser(RegisterFormDto userForm) {
        if (repository.existsUserByEmail(userForm.getEmail())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + userForm.getEmail() + " is exists.");
        User user = repository.save(modelMapper.map(userForm, User.class));
        entityManager.refresh(user);
        return modelMapper.map(user, ResponseUserDto.class);
    }
}
