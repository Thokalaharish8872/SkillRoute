package com.example.career_path.career_path.dto;

import com.example.career_path.career_path.entity.CareerPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerPathResponse implements Serializable {

    private List<CareerPath> roles;

}

