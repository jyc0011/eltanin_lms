package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class User {
    @Id // 키값임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) //디비가 설정 해줄 것임
    private int id;
    private String name;
    private boolean is_admin;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserHomework> userHomeworks;
}