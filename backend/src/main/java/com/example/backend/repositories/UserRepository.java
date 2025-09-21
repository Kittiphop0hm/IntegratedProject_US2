package com.example.backend.repositories;

import com.example.backend.entities.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsUserByEmail(String email);
    public boolean existsUserByPassword(String Password);
    public User findUserByEmail(String email);
    public User findUserByFullName(String fullName);
}
