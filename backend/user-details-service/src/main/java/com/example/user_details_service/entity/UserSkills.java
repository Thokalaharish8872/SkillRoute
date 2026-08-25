package com.example.user_details_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_skills")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkills {

    @Id
    private int userId;

    private String userName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills_list", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_career_paths_list", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "career_path_id")
    private List<Integer> careerPaths = new ArrayList<>();
}
