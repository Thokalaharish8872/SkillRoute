package com.example.skillroute_api_gateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadMapRequest {
    private String roleTitle;
    private Integer userId;
}
