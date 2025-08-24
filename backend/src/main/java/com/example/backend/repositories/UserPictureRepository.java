package com.example.backend.repositories;

import com.example.backend.entities.Userpicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPictureRepository extends JpaRepository<Userpicture, Integer> {
    List<Userpicture> findUserpictureByUsers_Id(Integer usersId);
}
