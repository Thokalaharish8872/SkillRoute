package com.example.activity_service.repositories;

import com.example.activity_service.models.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepo extends JpaRepository<DailyActivity, Integer> {

    Optional<DailyActivity> findByUserIdAndDate(int userId, LocalDate today);

    Optional<List<DailyActivity>> findByUserId(Integer integer);
}

