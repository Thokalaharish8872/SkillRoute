package com.example.user_details_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsServiceResponse {
    private List<String> skills = new ArrayList<>();
    private List<Integer> careerPaths = new ArrayList<>();
}
