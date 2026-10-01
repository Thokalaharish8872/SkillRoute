package com.example.ai_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiServiceResponse implements Serializable {

    private List<CareerPathDto> roles;

}