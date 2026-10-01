package com.example.dashboard_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {

    private int id;
    private String activityType;
    private String title;
    private String skillName;
    private LocalDate createdAt;
}
