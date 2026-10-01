package com.example.activity_service.services;

import com.example.activity_service.repositories.StreakRepo;
import com.example.activity_service.models.Streak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StreakService {

    @Autowired
    StreakRepo repo;

//    @CachePut(value = "streak", key = "#userId")
    public boolean updateStreak(int userId) {
         Streak streak = repo.findByUserId(userId);

        if(streak == null){
            repo.save(new Streak(userId, 1, LocalDate.now()));
            return true;
        }
        else {

            LocalDate lastLogin = streak.getLastLogin();
            LocalDate today = LocalDate.now();

            int currentStreak = streak.getStreak();

            if (!today.isAfter(lastLogin)) {
                return false;
            }
            else if (today.minusDays(1).equals(lastLogin)) {
                repo.updateStreak(userId, currentStreak + 1, today);
            }
            else {
                repo.updateStreak(userId, 1, today);
            }

            return true;
        }
    }

    @Cacheable(value = "streak", key = "#userId")
    public int getStreak(int userId) {
        Integer streak = repo.getStreak(userId);
        if(streak == null){
            repo.save(new Streak(userId, 1, LocalDate.now()));
            return 1;
        }

        return streak;
    }
}

