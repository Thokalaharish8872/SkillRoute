package com.example.activity_service.repositories;

import com.example.activity_service.models.Streak;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface StreakRepo extends JpaRepository<Streak, Integer> {

    @Query("SELECT s.streak FROM Streak s WHERE s.userId = :userId")
    Integer getStreak(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Streak s SET s.streak = :streak, s.lastLogin = :lastLogin WHERE s.userId = :userId")
    void updateStreak(@Param("userId") Integer userId,
                     @Param("streak") int streak,
                     @Param("lastLogin") LocalDate lastLogin);

    Streak findByUserId(int userId);
}

