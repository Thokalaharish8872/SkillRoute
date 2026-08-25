package com.example.skillroute_api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodingProfiles {

    private Integer id;

    private String leetcode;
    private String github;
    private String codechef;
    private String codeforces;
}


