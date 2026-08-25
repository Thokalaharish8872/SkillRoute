package com.example.career_path.career_path.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String category;

    private String difficulty;

    private Integer estimatedHours;

    private String description;

    private String DocumentationResource;

    private List<String> videoResource;

    private String certificationUrl;

    private String prerequisites;

    private String demandLevel;
}

