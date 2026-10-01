package com.example.career_path.career_path.service;

import com.example.career_path.career_path.repository.ModuleRepo;
import com.example.career_path.career_path.repository.SkillsRepo;

import com.example.career_path.career_path.entity.Module;
import com.example.career_path.career_path.entity.Response.ModuleResponse;
import com.example.career_path.career_path.entity.Skills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService {

    @Autowired
    ModuleRepo repo;

    @Autowired
    SkillsService skillsService;

    public ModuleResponse getModule(int moduleId) throws Exception {
        Module module = repo.getReferenceById(moduleId);
        List<Skills> skills = skillsService.identifyMissingSkillsAndGenerate(module.getSkills());

        return new ModuleResponse(module, skills);
    }

    public List<Module> searchModules(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword);
    }
}


