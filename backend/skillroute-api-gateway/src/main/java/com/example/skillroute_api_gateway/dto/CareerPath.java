package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerPath {

    private Integer id;

    private String title;
    private String salaryRange;

    private double matchScore;

    private String growth;

    private List<String> skillsRequired;

}

