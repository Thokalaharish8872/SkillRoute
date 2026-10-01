package com.example.skillroute_api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skills {

    private Integer id;
    private String title;
    private String category;
    private String difficulty;
    private Integer estimatedHours;
    private String description;

    @JsonProperty("DocumentationResource")
    private String DocumentationResource;

    private List<String> videoResource;
    private String certificationUrl;
    private String prerequisites;
    private String demandLevel;
}
