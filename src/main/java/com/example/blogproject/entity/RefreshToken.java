package com.example.blogproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_token")
@Setter @Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "value")
    private String value;
    //토큰 만료 (true:만료, false:유효)
    @Column(name = "expired")
    private boolean expired = false;
    //토큰 폐지 (true:폐지, false:유효)
    @Column(name = "revoked")
    private boolean revoked = false;
}
