package com.example.activity_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProgress {

    @Id
    private int userId;

    private Double totalActiveTime;
    private int coursesCompleted;
    private int streak;

    private LocalDate lastActiveDate;
}

