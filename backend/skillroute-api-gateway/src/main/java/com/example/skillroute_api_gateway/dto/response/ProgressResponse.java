package com.example.skillroute_api_gateway.dto.response;

import com.example.skillroute_api_gateway.dto.DailyActivity;
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

