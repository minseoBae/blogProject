package com.example.blogproject.entity.post;

import com.example.blogproject.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tags")
@Getter @Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;  // 태그를 생성한 사용자

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;  // 이 태그가 할당된 게시글들
}