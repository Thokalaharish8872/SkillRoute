package com.example.career_path.career_path.repository;

import com.example.career_path.career_path.entity.CareerPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CareerPathRepo extends JpaRepository<CareerPath, Integer> {
    List<CareerPath> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT c FROM CareerPath c where c.title IN :skills")
    List<CareerPath> getCareerPathsInDB(List<String> skills);

}

