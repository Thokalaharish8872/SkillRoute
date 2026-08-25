package com.example.career_path.career_path.dto;

import com.example.career_path.career_path.entity.CareerPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiServiceResponse {

    List<CareerPath> roles;
}

