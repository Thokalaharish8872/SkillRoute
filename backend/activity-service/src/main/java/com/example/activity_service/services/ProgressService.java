package com.example.activity_service.services;

import com.example.activity_service.models.DailyActivity;
import com.example.activity_service.models.requests.ProgressUpdateRequest;
import com.example.activity_service.models.Response.ProgressResponse;
import com.example.activity_service.models.UserProgress;
import com.example.activity_service.repositories.ProgressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProgressService {

    @Autowired
    ProgressRepo repo;

    @Autowired
    DailyActivityService dailyActivityService;

    @Transactional
    public void updateActiveTime(ProgressUpdateRequest request) {

        int userId = request.getUserId();
        double durationSeconds = request.getDurationSeconds();
        double hours = durationSeconds / 3600;
        double formattedHours = getFormattedTime(hours);

        LocalDate today = LocalDate.now();

        UserProgress progress = getUserProgressByDate(userId, today);

        progress.setTotalActiveTime(progress.getTotalActiveTime() + formattedHours);

        dailyActivityService.updateUserActivity(userId, formattedHours);
    }

    public ProgressResponse getProgress(int userId) {
        LocalDate today = LocalDate.now();

        Double todayActiveTime = dailyActivityService.getActiveTimeByDate(userId, today);
        Double totalActiveTime = repo.getTotalActiveTime(userId);

        List<DailyActivity> userActivities = dailyActivityService.getAllActivitiesByUser(userId);

        return new ProgressResponse(
                userId,
                totalActiveTime,
                todayActiveTime,
                userActivities,
                0,
                0
                );
    }

    private double getFormattedTime(double totalHours){
        int hours = (int) totalHours;
        int min = (int)((totalHours - hours) * 60);

        return hours + (min * 0.01);
    }

    private UserProgress getUserProgressByDate(int userId, LocalDate date){
        return repo.findById(userId).orElseGet(() -> {
            UserProgress u = new UserProgress(
                    userId,
                    0.0,
                    0,
                    0,
                    date
            );
            return repo.save(u);
        });
    }
}

