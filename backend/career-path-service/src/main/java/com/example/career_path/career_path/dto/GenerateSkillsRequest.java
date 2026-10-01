package com.example.career_path.career_path.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateSkillsRequest {
    private List<String> skills;
}
