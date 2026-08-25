package com.example.career_path.career_path.entity.Response;

import com.example.career_path.career_path.entity.Module;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhasesResponse {
    private Integer id;

    private Integer phaseNumber;
    private String phaseName;
    private String description;

    private List<Module> modules;
}


