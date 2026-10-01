package com.example.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadMapDto implements Serializable {

    private Integer roleId;
    private String title;
    private String description;
    private Integer totalPhases;
    private List<PhaseDto> phases;
}
