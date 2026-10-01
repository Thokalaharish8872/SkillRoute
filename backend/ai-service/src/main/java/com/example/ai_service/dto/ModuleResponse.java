package com.example.ai_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleResponse implements Serializable {

//    private static final long serialVersionUID = 1L;

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
