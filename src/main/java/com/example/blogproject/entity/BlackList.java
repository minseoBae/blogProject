package com.example.blogproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "black_list")
@Entity
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invalid_refresh_token")
    private String invalidRefreshToken;

    public BlackList(String invalidRefreshToken) {
        this.invalidRefreshToken = invalidRefreshToken;
    }
}
