package com.example.dashboard_service.services;

import com.example.dashboard_service.client.ActivityClient;
import com.example.dashboard_service.client.CareerPathClient;
import com.example.dashboard_service.client.UserDetailsClient;
import com.example.dashboard_service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private UserDetailsClient userDetailsClient;

    @Autowired
    private CareerPathClient careerPathClient;

    public DashboardResponse getDashboardData(Integer userId) {
        logger.info("Fetching dashboard data for userId: {}", userId);

        // 1. Fetch Day Streak
        int streak = 0;
        try {
            Map<String, Object> streakMap = activityClient.getStreak(userId);
            if (streakMap != null && streakMap.containsKey("streak")) {
                Object sVal = streakMap.get("streak");
                if (sVal instanceof Number) {
                    streak = ((Number) sVal).intValue();
                }
            }
        } catch (Exception e) {
            logger.error("Failed to fetch streak for userId: {}", userId, e);
        }

        // 2. Fetch Skills Mastered Count
        int skillsMasteredCount = 0;
        try {
            GetSkillsResponseDto skillsRes = userDetailsClient.getUserSkills(userId);
            if (skillsRes != null && skillsRes.getSkills() != null) {
                skillsMasteredCount = skillsRes.getSkills().size();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch user skills for userId: {}", userId, e);
        }

        // 3. Fetch Career Matches Count
        int careerMatchesCount = 0;
        try {
            CareerPathResponseDto careerRes = careerPathClient.getRecommendations(userId, false);
            if (careerRes != null && careerRes.getRoles() != null) {
                careerMatchesCount = careerRes.getRoles().size();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch career recommendations for userId: {}", userId, e);
        }

        // 4. Fetch Recent Activities
        List<ActivityDto> recentActivities = new ArrayList<>();
        try {
            UserActivityDto activityRes = activityClient.getRecentActivity(userId);
            if (activityRes != null && activityRes.getRecentActivities() != null) {
                recentActivities = activityRes.getRecentActivities();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch recent activities for userId: {}", userId, e);
        }

        return new DashboardResponse(userId, streak, skillsMasteredCount, careerMatchesCount, recentActivities);
    }
}
