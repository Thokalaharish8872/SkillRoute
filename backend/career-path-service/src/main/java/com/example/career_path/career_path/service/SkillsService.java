package com.example.career_path.career_path.service;

import com.example.career_path.career_path.dto.GenerateSkillsRequest;
import com.example.career_path.career_path.repository.SkillsRepo;
import com.example.career_path.career_path.entity.Skills;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SkillsService.class);

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

        if (!skillsMissingInDB.isEmpty()) {
            GenerateSkillsRequest request = new GenerateSkillsRequest(skillsMissingInDB);
            List<Skills> generatedSkills = aiClient.generateSkills(skillsMissingInDB);

            logger.info("generated skills : {}", generatedSkills);
            if (generatedSkills != null && !generatedSkills.isEmpty()) {
                List<Skills> skillsAddedToDB = repo.saveAll(generatedSkills);
                logger.info("skills Added to DB : {}", skillsAddedToDB);
                skills.addAll(skillsAddedToDB);
            }
        }

        logger.info("all skills : {}", skills);

        return skills;
    }

    public Skills getSkill(int skillId) {
        return repo.getReferenceById(skillId);
    }

    public List<Skills> searchSkills(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword);
    }
}


