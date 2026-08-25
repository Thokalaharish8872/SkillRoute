package com.example.skillroute_api_gateway.dto.response;

import com.example.skillroute_api_gateway.dto.CareerPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerPathResponse {

    private List<CareerPath> roles;

}

