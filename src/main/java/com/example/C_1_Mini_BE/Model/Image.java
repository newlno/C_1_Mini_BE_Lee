package com.example.C_1_Mini_BE.Model;


import com.example.C_1_Mini_BE.Validator.URLvalidator;
import com.example.C_1_Mini_BE.Validator.ValidateObject;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "postImage")
public class Image extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String imgURL;

    public Image(Post post, String imageUrl){
        URLvalidator.isValidURL(imageUrl);
        ValidateObject.postValidate(post);
        this.post=post;
        this.imgURL = imageUrl;
    }
}
