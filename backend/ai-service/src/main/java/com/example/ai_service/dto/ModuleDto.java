package com.example.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDto implements Serializable {

    private Integer id;
    private Integer durationWeeks;
    private String title;
    private List<String> skills;
}
