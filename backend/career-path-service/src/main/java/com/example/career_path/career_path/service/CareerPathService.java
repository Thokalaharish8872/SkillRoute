package com.example.career_path.career_path.service;

import com.example.career_path.career_path.client.AIClient;
import com.example.career_path.career_path.client.UserDetailsClient;
import com.example.career_path.career_path.client.UserSkillsClient;
import com.example.career_path.career_path.dto.*;
import com.example.career_path.career_path.entity.CareerPath;
import com.example.career_path.career_path.repository.CareerPathRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CareerPathService {

    @Autowired
    CareerPathRepo repo;

    UserDetailsClient userDetailsClient;
    AIClient aiClient;

    public CareerPathService(UserDetailsClient userDetailsClient, AIClient aiClient){
        this.userDetailsClient = userDetailsClient;
        this.aiClient = aiClient;
    }

    public CareerPathResponse getRecommendations(int userId) throws Exception {

        System.out.println("Getting Recommendations for user");

        UserDetailsServiceResponse userDetails = userDetailsClient.getSkillsAndCareerPaths(userId);

        if(userDetails.getSkills().isEmpty()){
            throw new Exception("user skills Empty");
        }

        if(!userDetails.getCareerPaths().isEmpty()){
            System.out.println("Getting Recommendations from DB");
            return getRecommendationsFromDB(userDetails.getCareerPaths());
        }
        else{
            System.out.println("Generating Recommendations with AI");
            return getRecommendationsFromAI(userId, userDetails.getSkills());
        }
    }

    public CareerPathResponse regenerateRecommendations(int userId) throws Exception {
        System.out.println("Regenerating Recommendations for user based on updated skills: " + userId);
        UserDetailsServiceResponse userDetails = userDetailsClient.getSkillsAndCareerPaths(userId);

        if(userDetails.getSkills().isEmpty()){
            throw new Exception("user skills Empty");
        }

        return getRecommendationsFromAI(userId, userDetails.getSkills());
    }

    private CareerPathResponse getRecommendationsFromDB(List<Integer> skills) {

        List<CareerPath> roles = new ArrayList<>();

        for (int roleId : skills) {
            roles.add(repo.getReferenceById(roleId));
        }

        System.out.println(roles);
        return new CareerPathResponse(roles);
    }

    private CareerPathResponse getRecommendationsFromAI(int userId, List<String> skills) throws Exception {

        Set<String> notInDB = new HashSet<>(skills);

        List<CareerPath> inDB = repo.getCareerPathsInDB(skills);
        inDB.forEach(notInDB::remove);

        List<CareerPath> careerPaths = new ArrayList<>(inDB);

        System.out.println(notInDB.size());
        if(!notInDB.isEmpty()) {
            AiServiceResponse response = aiClient.generateRecommendations(new GenerateCareerPathRequest(notInDB));
            careerPaths.addAll(response.getRoles());
            repo.saveAll(response.getRoles());
        }

        System.out.println(careerPaths);
        List<Integer> careerPathIds = new ArrayList<>(careerPaths.stream()
                .map(CareerPath::getId)
                .toList());

        userDetailsClient.updateCareerPaths(userId, careerPathIds);

        return new CareerPathResponse(careerPaths);
    }

    public List<CareerPath> searchCareerPaths(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword);
    }
}

