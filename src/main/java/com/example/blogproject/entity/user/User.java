package com.example.blogproject.entity.user;

import com.example.blogproject.entity.post.Post;
import com.example.blogproject.entity.post.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "profile_image")
    private String profileImage;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "blog_name")
    private String blogName;
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tag> tags;  // 사용자가 생성한 태그들
}