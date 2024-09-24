package com.likelion.lms.Repository;

import com.likelion.lms.Domain.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    // 페이징을 지원하는 findAll 메소드, 페이지 정보에 맞춰 모든 과제를 조회
    Page<Homework> findAll(Pageable pageable);
}