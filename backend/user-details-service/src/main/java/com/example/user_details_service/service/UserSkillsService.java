package com.example.user_details_service.service;

import com.example.user_details_service.controller.UserSkillsController;
import com.example.user_details_service.dto.UserDetailsServiceResponse;
import com.example.user_details_service.entity.Response.AddSkillResponse;
import com.example.user_details_service.entity.Response.GetSkillsResponse;
import com.example.user_details_service.entity.Response.RemoveSkillsResponse;
import com.example.user_details_service.exception.UserNotFoundException;
import com.example.user_details_service.repository.UserSkillsRepo;
import com.example.user_details_service.entity.UserSkills;
import com.example.user_details_service.entity.requests.AddSkillRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserSkillsService {
    private static final Logger logger = LoggerFactory.getLogger(UserSkillsController.class);

    @Autowired
    UserSkillsRepo userSkillsRepo;

    @CachePut(value = "user_skills", key = "#request.getUserId()")
    public GetSkillsResponse addSkill(AddSkillRequest request) throws Exception {
        int userId = request.getUserId();
        String skillToAdd = request.getSkillName();

        UserSkills skills = userSkillsRepo.findById(userId).orElse(new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));

        if (skills.getSkills() == null) {
            skills.setSkills(new ArrayList<>());
        }

        System.out.println(skills.getSkills());
        if (skills.getSkills().contains(skillToAdd)) {
            throw new Exception("Skill Already Exist");
        }

        skills.getSkills().add(skillToAdd);
        userSkillsRepo.save(skills);

        List<String> skill = skills.getSkills() != null ? new ArrayList<>(skills.getSkills()) : new ArrayList<>();
        return new GetSkillsResponse(userId, skill);
    }

    @CachePut(value = "user_skills", key = "#userId")
    public GetSkillsResponse removeUserSkill(int userId, String skillName) {

        UserSkills skillsModel = userSkillsRepo.findById(userId).orElse(new UserSkills());
        List<String> skills = skillsModel.getSkills() != null ? new ArrayList<>(skillsModel.getSkills()) : new ArrayList<>();
        skills.remove(skillName);
        skillsModel.setSkills(skills);
        userSkillsRepo.save(skillsModel);

        return new GetSkillsResponse(userId, skills);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user_skills", key = "#userId")
    public GetSkillsResponse getUserSkills(int userId) {
        System.out.println("Gathering user Skills for userId: " + userId);
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseGet(() -> new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));
        List<String> skills = userSkills.getSkills() != null ? new ArrayList<>(userSkills.getSkills()) : new ArrayList<>();
        return new GetSkillsResponse(userId, skills);
    }

//    @Transactional(readOnly = true)
//    @Cacheable(value = "skills_and_career_paths", key = "#userId")
    public UserDetailsServiceResponse getSkillsAndCareerPaths(int userId) {
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseGet(() -> new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));

        List<String> skills = userSkills.getSkills() != null ? new ArrayList<>(userSkills.getSkills()) : new ArrayList<>();
        List<Integer> careerPaths = userSkills.getCareerPaths() != null ? new ArrayList<>(userSkills.getCareerPaths()) : new ArrayList<>();

        logger.info("user skills and career paths response done");
        return new UserDetailsServiceResponse(skills, careerPaths);
    }

    public void updateCareerPaths(int userId, List<Integer> careerPaths) throws Exception {
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        userSkills.setCareerPaths(careerPaths);
        userSkillsRepo.save(userSkills);
    }
}


