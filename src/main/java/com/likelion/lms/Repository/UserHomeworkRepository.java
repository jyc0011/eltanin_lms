package com.likelion.lms.Repository;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.UserHomework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHomeworkRepository extends JpaRepository<UserHomework, Long> {
    List<UserHomework> findByHomeworkId(Long homeworkId);
    List<UserHomework> findByHomework(Homework homework);
    List<UserHomework> findByHomeworkIdAndUserId(Long homeworkId, Long userId);
}
