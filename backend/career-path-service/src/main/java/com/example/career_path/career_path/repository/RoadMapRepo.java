package com.example.career_path.career_path.repository;

import com.example.career_path.career_path.entity.RoadMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoadMapRepo extends JpaRepository<RoadMap, Integer> {

    RoadMap findByTitle(String title);
}


