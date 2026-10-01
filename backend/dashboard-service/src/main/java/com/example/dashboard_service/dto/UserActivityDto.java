package com.example.dashboard_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityDto {

    private Integer userId;
    private List<ActivityDto> recentActivities;
}
