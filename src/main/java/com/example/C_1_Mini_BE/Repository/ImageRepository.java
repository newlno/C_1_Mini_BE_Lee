package com.example.C_1_Mini_BE.Repository;

import com.example.C_1_Mini_BE.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    Optional<Image> findByImgURL(String imgUrl);
}
