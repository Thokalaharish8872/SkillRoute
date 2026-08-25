package com.example.skillroute_api_gateway.dto.response;

import com.example.skillroute_api_gateway.dto.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityResponse {

    private Integer userId;
    private List<Activity> recentActivities;
}

