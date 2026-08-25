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
public class Phases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer phaseNumber;
    private String phaseName;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "phase_id")
    private List<Module> modules;
}


