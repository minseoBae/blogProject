package com.example.blogproject.entity.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;


    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}