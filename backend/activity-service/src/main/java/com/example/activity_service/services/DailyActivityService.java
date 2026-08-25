package com.example.activity_service.services;

import com.example.activity_service.models.DailyActivity;
import com.example.activity_service.repositories.DailyActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyActivityService {

    @Autowired
    DailyActivityRepo repo;

    @Transactional
    public void updateUserActivity(int userId, Double hours){

        LocalDate today = LocalDate.now();

        DailyActivity activity = getActivityByUserIdAndDate(userId, today);
        activity.setActiveTime(activity.getActiveTime() + hours);
    }

    public List<DailyActivity> getAllActivitiesByUser(int userId){
        return getUserActivities(userId);
    }

    public Double getActiveTimeByDate(int userId, LocalDate date) {
         return getActivityByUserIdAndDate(userId, date).getActiveTime();
    }

    @Transactional
    public DailyActivity getActivityByUserIdAndDate(int userId, LocalDate date){
        return repo.findByUserIdAndDate(
                        userId,
                        date
                ).orElseGet(() -> {
                    DailyActivity d = new DailyActivity();
                    d.setUserId(userId);
                    d.setDate(date);
                    d.setActiveTime(0.0);

                    return repo.save(d);
                }
        );
    }

    private List<DailyActivity> getUserActivities(int userId){
        return repo.findByUserId(userId).orElse(new ArrayList<>());
    }
}

