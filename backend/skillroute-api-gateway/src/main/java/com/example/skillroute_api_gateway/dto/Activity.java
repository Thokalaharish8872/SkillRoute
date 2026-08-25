package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    private int id;

    private String activityType;
    private String title;
    private String skillName;
    private LocalDate createdAt;
}

