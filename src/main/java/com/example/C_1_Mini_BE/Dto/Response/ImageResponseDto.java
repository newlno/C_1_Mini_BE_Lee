package com.example.C_1_Mini_BE.Dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private String imageUrl;

   public ImageResponseDto(String imageUrl){
       this.imageUrl = imageUrl;

   }
}
