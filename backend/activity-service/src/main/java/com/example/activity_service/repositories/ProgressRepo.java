package com.example.activity_service.repositories;

import com.example.activity_service.models.UserProgress;
import com.example.activity_service.projections.ActiveTimeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProgressRepo extends JpaRepository<UserProgress, Integer> {

    @Query("SELECT u.totalActiveTime FROM UserProgress u WHERE u.userId = :userId")
    Double getTotalActiveTime(int userId);

    @Transactional
    @Modifying
    @Query("UPDATE UserProgress u SET u.totalActiveTime = u.totalActiveTime + :hours WHERE u.userId = :userId")
    void updateTotalActiveTime(int userId,
                          Double hours);
}

