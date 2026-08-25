package com.example.career_path.career_path.service;

import com.example.career_path.career_path.repository.SkillsRepo;
import com.example.career_path.career_path.entity.Skills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillsService {

    @Autowired
    SkillsRepo repo;

    @Autowired
    com.example.career_path.career_path.client.AIClient aiClient;

    public List<Skills> identifyMissingSkillsAndGenerate(List<String> skillsRequired) throws Exception {
        List<Skills> skills = new ArrayList<>();
        List<String> skillsMissingInDB = new ArrayList<>();

        for (String skill : skillsRequired) {
            Skills skills1 = repo.findByTitle(skill);
            if (skills1 == null) {
                skillsMissingInDB.add(skill);
            } else {
                skills.add(skills1);
            }
        }

        System.out.println(skillsMissingInDB);
        
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        if (!skillsMissingInDB.isEmpty()) {
            Object rawSkills = aiClient.generateSkills(skillsMissingInDB);
            List<Skills> skillsAddedToDB = mapper.convertValue(
                    rawSkills,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Skills>>() {}
            );
            skillsAddedToDB = repo.saveAll(skillsAddedToDB);
            skills.addAll(skillsAddedToDB);
        }

        return skills;
    }

    public Skills getSkill(int skillId) {
        return repo.getReferenceById(skillId);
    }

    public List<Skills> searchSkills(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword);
    }
}


