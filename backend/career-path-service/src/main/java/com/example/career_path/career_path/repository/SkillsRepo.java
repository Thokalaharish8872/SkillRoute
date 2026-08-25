package com.example.career_path.career_path.repository;

import com.example.career_path.career_path.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillsRepo  extends JpaRepository<Skills, Integer> {

    Skills findByTitle(String skill);

    List<Skills> findByTitleContainingIgnoreCase(String keyword);
}


