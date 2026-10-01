package com.example.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private String profilePicture;
    private LocalDate joinDate;

    private LocalDate lastLogin;

    private Integer streak;
    private Integer roadmapProgress;
    private Integer skillsLearned;
    private Integer assessmentCompleted;

    @ElementCollection
    private List<Integer> careerPaths;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}