package com.example.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String userName;
    private String email;
    private String role;
    private String location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profiles_id")
    private CodingProfiles codingProfiles;
}
