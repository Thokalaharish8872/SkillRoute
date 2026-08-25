package com.example.career_path.career_path.service;

import com.example.career_path.career_path.mappers.PhasesMapper;
import com.example.career_path.career_path.entity.Response.PhasesResponse;
import com.example.career_path.career_path.repository.PhasesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhasesService {

    @Autowired
    PhasesRepo repo;

    @Autowired
    PhasesMapper mapper;

    public PhasesResponse getPhase(int moduleId) {
        return mapper.toDto(repo.getReferenceById(moduleId));
    }
}


