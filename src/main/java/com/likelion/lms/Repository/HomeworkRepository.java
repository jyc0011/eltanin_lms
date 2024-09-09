package com.likelion.lms.Repository;

import com.likelion.lms.Domain.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    Page<Homework> findAll(Pageable pageable);
}