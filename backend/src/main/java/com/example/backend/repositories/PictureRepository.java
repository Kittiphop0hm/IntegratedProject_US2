package com.example.backend.repositories;

import com.example.backend.entities.Picture;
import com.example.backend.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PictureRepository extends JpaRepository<Picture, Integer> {
    List<Picture> findPictureBySalesId(Integer id);
}
