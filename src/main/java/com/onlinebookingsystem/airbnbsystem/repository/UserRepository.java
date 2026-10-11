package com.onlinebookingsystem.airbnbsystem.repository;

import com.onlinebookingsystem.airbnbsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
