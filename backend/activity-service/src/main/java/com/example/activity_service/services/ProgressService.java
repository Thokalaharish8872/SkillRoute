package com.example.activity_service.services;

import com.example.activity_service.models.DailyActivity;
import com.example.activity_service.models.requests.ProgressUpdateRequest;
import com.example.activity_service.models.Response.ProgressResponse;
import com.example.activity_service.models.UserProgress;
import com.example.activity_service.repositories.ProgressRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProgressService {

    private static final Logger logger = LoggerFactory.getLogger(ProgressService.class);
    @Autowired
    ProgressRepo repo;

    @Autowired
    DailyActivityService dailyActivityService;

    @Transactional
    @CacheEvict(value = "user_progress", key = "#request.getUserId()")
    public void updateActiveTime(ProgressUpdateRequest request) {

        int userId = request.getUserId();
        Double durationSecondsObj = request.getDurationSeconds();
        double durationSeconds = durationSecondsObj != null ? durationSecondsObj : 0.0;
        if (durationSeconds <= 0 || userId == 0) {
            return;
        }

        double addedHours = durationSeconds / 3600.0;

        LocalDate today = LocalDate.now();

        UserProgress progress = getUserProgressByDate(userId, today);
        double currentTotal = progress.getTotalActiveTime() != null ? progress.getTotalActiveTime() : 0.0;
        progress.setTotalActiveTime(currentTotal + addedHours);
        progress.setLastActiveDate(today);

        dailyActivityService.updateUserActivity(userId, addedHours);

        logger.info("updated activity time for userId : {} (+{} sec)", userId, durationSeconds);
    }

    @Cacheable(value = "user_progress", key = "#userId")
    public ProgressResponse getProgress(int userId) throws Exception{
        LocalDate today = LocalDate.now();

        Double rawTodayActiveTime = dailyActivityService.getActiveTimeByDate(userId, today);
        Double rawTotalActiveTime = repo.getTotalActiveTime(userId);
        if (rawTodayActiveTime == null) rawTodayActiveTime = 0.0;
        if (rawTotalActiveTime == null) rawTotalActiveTime = 0.0;

        Double todayActiveTime = formatToHoursAndMinutes(rawTodayActiveTime);
        Double totalActiveTime = formatToHoursAndMinutes(rawTotalActiveTime);

        List<DailyActivity> rawActivities = dailyActivityService.getAllActivitiesByUser(userId);
        List<DailyActivity> userActivities = rawActivities.stream().map(a -> {
            DailyActivity formatted = new DailyActivity();
            formatted.setActivityId(a.getActivityId());
            formatted.setUserId(a.getUserId());
            formatted.setDate(a.getDate());
            formatted.setActiveTime(formatToHoursAndMinutes(a.getActiveTime() != null ? a.getActiveTime() : 0.0));
            return formatted;
        }).toList();

        logger.info("returned progress for userId : {}", userId);

        return new ProgressResponse(
                userId,
                totalActiveTime,
                todayActiveTime,
                userActivities,
                0,
                0
                );
    }

    private double formatToHoursAndMinutes(double decimalHours) {
        if (decimalHours <= 0) return 0.0;
        long totalSeconds = Math.round(decimalHours * 3600.0);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;

        return hours + (minutes * 0.01);
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

