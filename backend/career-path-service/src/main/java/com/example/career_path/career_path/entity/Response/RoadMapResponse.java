package com.example.career_path.career_path.entity.Response;

import com.example.career_path.career_path.entity.Phases;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadMapResponse {

    private int roleId;

    private String title;
    private String description;

    private int totalPhases;
    private List<Phases> phases;

}


