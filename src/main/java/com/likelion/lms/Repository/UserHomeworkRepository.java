package com.likelion.lms.Repository;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.UserHomework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//jpa repository라는 인터페이스를 이용해 db와의 연동을 간단하게 함
public interface UserHomeworkRepository extends JpaRepository<UserHomework, Long> {
    // 특정 과제 ID로 제출된 사용자 과제 리스트를 조회
    List<UserHomework> findByHomeworkId(Long homeworkId);

    // 특정 Homework 엔티티로 제출된 사용자 과제 리스트를 조회
    List<UserHomework> findByHomework(Homework homework);

    // 특정 과제 ID와 사용자 ID로 제출된 사용자 과제를 조회
    List<UserHomework> findByHomeworkIdAndUserId(Long homeworkId, Long userId);
}