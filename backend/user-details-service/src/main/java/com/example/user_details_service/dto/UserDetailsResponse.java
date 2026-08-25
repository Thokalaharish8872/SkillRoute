package com.example.user_details_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {

    private List<String> skills;
    private List<Integer> careerPaths;
}
