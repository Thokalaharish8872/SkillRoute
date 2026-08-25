package com.example.career_path.career_path.repository;

import com.example.career_path.career_path.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Integer> {
    List<Module> findByTitleContainingIgnoreCase(String keyword);
}


