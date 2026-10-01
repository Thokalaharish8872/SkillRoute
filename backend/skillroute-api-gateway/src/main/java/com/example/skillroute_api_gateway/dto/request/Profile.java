package com.example.skillroute_api_gateway.dto.request;

import com.example.skillroute_api_gateway.dto.CodingProfiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private Integer id;

    private Integer userId;
    private String userName;
    private String email;
    private String role;
    private String location;

    private CodingProfiles codingProfiles;
}


