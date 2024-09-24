package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity // JPA가 관리하는 엔티티 클래스임을 명시
@Table(name = "homework") // 데이터베이스에서 사용할 테이블명을 지정
@Data // Lombok을 사용하여 Getter, Setter, Equals, HashCode, ToString 자동 생성
@AllArgsConstructor // 모든 필드를 사용하는 생성자 자동 생성
@NoArgsConstructor // 파라미터가 없는 기본 생성자 자동 생성
public class Homework {

    @Id // 해당 필드가 기본 키(primary key)임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임 (자동 증가)
    private Long id; // 과제의 ID, 자동 생성되는 기본 키

    @Column(nullable = false)
    private String cate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String dueTime;

    // 일대다 관계임
    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL)
    // mappedBy는 연관된 엔티티에서 참조할 필드를 지정
    // CascadeType.ALL로 과제 삭제 시 연관된 제출물도 함께 삭제됨
    @ToString.Exclude // ToString 메서드에서 제외하여 순환 참조를 방지
    private List<UserHomework> userHomeworks; // 제출된 사용자 과제 목록
}
