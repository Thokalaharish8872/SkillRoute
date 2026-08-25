package com.example.activity_service.models.Response;

import com.example.activity_service.models.DailyActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponse {

    private int userId;
    private Double totalActiveTime;
    private Double todayActiveTime;
    private List<DailyActivity> dailyActivities;
    private int coursesCompleted;
    private int streak;
}

