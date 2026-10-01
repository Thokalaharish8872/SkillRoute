package com.example.career_path.career_path.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerPath implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String salaryRange;

    @Transient
    private double matchScore;

    private String growth;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skillsRequired;

}

