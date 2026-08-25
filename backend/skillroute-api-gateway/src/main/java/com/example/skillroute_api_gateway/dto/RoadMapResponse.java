package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
