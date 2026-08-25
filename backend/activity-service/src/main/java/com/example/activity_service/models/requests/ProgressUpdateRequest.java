package com.example.activity_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressUpdateRequest {

    private int userId;
    private Double durationSeconds;
}

