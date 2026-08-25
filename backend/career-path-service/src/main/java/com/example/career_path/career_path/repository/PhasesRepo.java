package com.example.career_path.career_path.repository;

import com.example.career_path.career_path.entity.Phases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhasesRepo extends JpaRepository<Phases, Integer> {

    @Query(value = "SELECT * FROM phases WHERE role_id = :id", nativeQuery = true)
    List<Phases> findByRoleId(@Param("id") int id);
}


