package com.example.user_details_service.entity.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveSkillRequest {

    int userId;
    String skillName;
}
