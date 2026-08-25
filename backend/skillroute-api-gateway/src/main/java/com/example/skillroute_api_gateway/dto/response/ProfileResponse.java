package com.example.skillroute_api_gateway.dto.response;

import com.example.skillroute_api_gateway.dto.CodingProfiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private Integer userId;
    private String userName;
    private String email;
    private String role;
    private String location;
    private CodingProfiles codingProfiles;
}


