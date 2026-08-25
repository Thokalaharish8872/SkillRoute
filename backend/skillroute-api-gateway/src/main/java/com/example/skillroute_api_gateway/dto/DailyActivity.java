package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyActivity {

    int activityId;

    int userId;

    LocalDate date;

    Double activeTime;
}

