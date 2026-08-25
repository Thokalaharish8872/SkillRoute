package com.example.user_details_service.entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsResponse {

    private int userId;

    private List<String> skills;
}


