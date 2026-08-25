package com.example.user_details_service.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSkillRequest {

    private int userId;
    private String skillName;
}


