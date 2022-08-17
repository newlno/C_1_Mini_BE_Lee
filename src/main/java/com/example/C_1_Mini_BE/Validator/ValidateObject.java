package com.example.C_1_Mini_BE.Validator;


import com.example.C_1_Mini_BE.Model.Post;

public class  ValidateObject  {


    public static void  postValidate(Post post){
        if(post.getId()==null || post.getId()<=0){
            throw  new IllegalArgumentException("유효하지 않는 Post Id입니다.");
        }
    }
}
