package com.example.activity_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "daily_activity",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "date"})
        }
)
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int activityId;

    @Column(name = "user_id", nullable = false)
    int userId;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(nullable = false)
    Double activeTime;
}

