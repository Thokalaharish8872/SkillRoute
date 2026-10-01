package com.example.dashboard_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Integer userId;
    private int streak;
    private int skillsMasteredCount;
    private int careerMatchesCount;
    private List<ActivityDto> recentActivities;
}
