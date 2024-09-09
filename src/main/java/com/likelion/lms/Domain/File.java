package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    @ManyToOne
    @JoinColumn(name = "user_homework_id")
    private UserHomework userHomework;
    public File(String fileName, String string) {
        this.fileName = fileName;
        this.filePath = string;
    }
}