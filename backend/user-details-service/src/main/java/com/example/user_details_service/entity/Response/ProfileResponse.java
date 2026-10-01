package com.example.user_details_service.entity.Response;

import com.example.user_details_service.entity.CodingProfiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse implements Serializable {

    private Integer userId;
    private String userName;
    private String email;
    private String role;
    private String location;
    private CodingProfiles codingProfiles;
}


