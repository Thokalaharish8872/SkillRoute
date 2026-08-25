package com.example.user_details_service.service;

import com.example.user_details_service.dto.UserDetailsServiceResponse;
import com.example.user_details_service.entity.Response.AddSkillResponse;
import com.example.user_details_service.entity.Response.GetSkillsResponse;
import com.example.user_details_service.entity.Response.RemoveSkillsResponse;
import com.example.user_details_service.exception.UserNotFoundException;
import com.example.user_details_service.repository.UserSkillsRepo;
import com.example.user_details_service.entity.UserSkills;
import com.example.user_details_service.entity.Response.UserSkillsResponse;
import com.example.user_details_service.entity.requests.AddSkillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSkillsService {

    @Autowired
    UserSkillsRepo userSkillsRepo;

    public AddSkillResponse addSkill(AddSkillRequest request) throws Exception {
        int userId = request.getUserId();
        String skillToAdd = request.getSkillName();

        UserSkills skills = userSkillsRepo.findById(userId).orElse(new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));

        if (skills.getSkills() == null) {
            skills.setSkills(new ArrayList<>());
        }

        if (skills.getSkills().contains(skillToAdd)) {
            throw new Exception("Skill Already Exist");
        }

        skills.getSkills().add(skillToAdd);
        UserSkills saved = userSkillsRepo.save(skills);

        return new AddSkillResponse(userId, skills.getSkills());
    }

    public GetSkillsResponse getUserSkills(int userId) {
        System.out.println("Gathering user Skills for userId: " + userId);
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseGet(() -> new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));
        return new GetSkillsResponse(userId, userSkills.getSkills());
    }

    public UserDetailsServiceResponse getSkillsAndCareerPaths(int userId) {
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseGet(() -> new UserSkills(userId, "User", new ArrayList<>(), new ArrayList<>()));

        List<String> skills = userSkills.getSkills() != null ? userSkills.getSkills() : new ArrayList<>();
        List<Integer> careerPaths = userSkills.getCareerPaths() != null ? userSkills.getCareerPaths() : new ArrayList<>();

        return new UserDetailsServiceResponse(skills, careerPaths);
    }

    public void updateCareerPaths(int userId, List<Integer> careerPaths) throws Exception{
        UserSkills userSkills = userSkillsRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        userSkills.setCareerPaths(careerPaths);
        userSkillsRepo.save(userSkills);
    }

    public RemoveSkillsResponse removeUserSkill(int userId, String skillName) {

        UserSkills skillsModel = userSkillsRepo.findById(userId).orElse(new UserSkills());
        List<String> skills = skillsModel.getSkills();
        skills.remove(skillName);
        userSkillsRepo.save(skillsModel);

        return new RemoveSkillsResponse(userId, skills);
    }
}


