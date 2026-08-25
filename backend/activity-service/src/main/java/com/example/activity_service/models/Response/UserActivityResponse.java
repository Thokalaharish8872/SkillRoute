package com.example.activity_service.models.Response;

import com.example.activity_service.models.Activity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityResponse {

    private Integer userId;
    private List<Activity> recentActivities;
}

