package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_homework")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHomework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "homework_id")
    @ToString.Exclude
    private Homework homework;

    @OneToMany(mappedBy = "userHomework", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<File> files;
}
