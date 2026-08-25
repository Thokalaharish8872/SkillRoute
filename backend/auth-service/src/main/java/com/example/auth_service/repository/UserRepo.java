package com.example.auth_service.repository;

import com.example.auth_service.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserRepo extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.streak = :streak WHERE u.id = :id")
    int updateStreak(@Param("id") Integer id,
                     @Param("streak") int streak);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :id")
    void updateLastLogin(@Param("id") Integer id,
                         @Param("lastLogin") LocalDate lastLogin);

    User findByUsername(String username);

    User findByEmail(String email);
}