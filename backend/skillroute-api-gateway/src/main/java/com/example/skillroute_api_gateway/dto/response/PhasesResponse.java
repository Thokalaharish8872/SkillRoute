package com.example.skillroute_api_gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhasesResponse {
    private Integer id;

    private Integer phaseNumber;
    private String phaseName;
    private String description;

    private List<Module> modules;
}


