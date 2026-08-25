package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Module {
    private int id;
    private String title;
    private String description;
    private int phaseId;
}
