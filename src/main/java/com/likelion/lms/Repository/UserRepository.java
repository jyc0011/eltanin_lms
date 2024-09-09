package com.likelion.lms.Repository;

import com.likelion.lms.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
