package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.Module;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Phases {
    private Integer id;

    private Integer phaseNumber;
    private String phaseName;
    private String description;

    private List<Module> modules;
}


