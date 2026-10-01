package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    private Integer id;
    private Integer durationWeeks;
    private String title;
    private List<String> skills;
}
