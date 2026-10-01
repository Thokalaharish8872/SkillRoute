package com.example.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDto implements Serializable {

    private Integer id;
    private Integer phaseNumber;
    private String phaseName;
    private String description;
    private List<ModuleDto> modules;
}
