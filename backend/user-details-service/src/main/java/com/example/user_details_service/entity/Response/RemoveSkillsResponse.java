package com.example.user_details_service.entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveSkillsResponse implements Serializable {

    int userId;
    List<String> skills;
}
