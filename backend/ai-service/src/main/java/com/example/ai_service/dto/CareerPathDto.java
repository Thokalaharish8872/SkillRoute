package com.example.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerPathDto {

    private String title;
    private Double matchScore;
    private String growth;
    private String salaryRange;
    private List<String> skillsRequired;

}