package com.example.user_details_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserDetails {

    @Id
    private Integer userId;

    @ElementCollection
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    private List<Integer> careerPaths = new ArrayList<>();
}
