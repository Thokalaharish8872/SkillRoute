package com.example.career_path.career_path.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsServiceResponse {

    List<Integer> careerPaths;
    List<String> skills;
}

