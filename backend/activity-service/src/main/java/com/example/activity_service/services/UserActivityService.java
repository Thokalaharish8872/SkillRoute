package com.example.activity_service.services;

import com.example.activity_service.mappers.UserMapper;
import com.example.activity_service.models.Response.UserActivityResponse;
import com.example.activity_service.repositories.ActivityRepo;
import com.example.activity_service.models.*;
import com.example.activity_service.models.Response.ActivityResponse;
import com.example.activity_service.models.requests.UserActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserActivityService {

    @Autowired
    ActivityRepo repo;

    @Autowired
    StreakService streakService;

    @Autowired
    UserMapper mapper;

    public ActivityResponse updateActivity(UserActivityRequest request) {
        int userId = request.getUserId();
        int skillId = request.getSkillId();

        UserActivity activity = repo.findById(userId).orElse(new UserActivity(userId, new LinkedList<>()));

        List<Activity> recentActivities = activity.getRecentActivities();
        if(recentActivities.size() > 5){
            recentActivities.removeFirst();
        }

        recentActivities.add(new Activity(
                0,
                "new Skills",
                "Added Skill #" + skillId + " to skills",
                "Skill #" + skillId,
                LocalDate.now()
                )
        );

        repo.save(activity);
        boolean isFirstTask = streakService.updateStreak(userId);
        int streak = streakService.getStreak(userId);

        return new ActivityResponse(isFirstTask, streak);

    }

    public UserActivityResponse getRecentActivity(int userId) {
        return mapper.toDto(repo.getReferenceById(userId));
    }
}

