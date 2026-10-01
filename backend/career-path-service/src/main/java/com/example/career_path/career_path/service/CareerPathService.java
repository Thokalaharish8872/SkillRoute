package com.example.career_path.career_path.service;

import com.example.career_path.career_path.client.AIClient;
import com.example.career_path.career_path.client.UserDetailsClient;
import com.example.career_path.career_path.controller.CareerPathController;
import com.example.career_path.career_path.dto.*;
import com.example.career_path.career_path.entity.CareerPath;
import com.example.career_path.career_path.repository.CareerPathRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CareerPathService {
    private static final Logger logger = LoggerFactory.getLogger(CareerPathService.class);

    @Autowired
    CareerPathRepo repo;

    UserDetailsClient userDetailsClient;
    AIClient aiClient;


    public CareerPathService(UserDetailsClient userDetailsClient, AIClient aiClient){
        this.userDetailsClient = userDetailsClient;
        this.aiClient = aiClient;
    }

//    @Transactional(readOnly = true)
    @Cacheable(value = "career_recommendations", key = "#userId")
    public CareerPathResponse getRecommendations(int userId) throws Exception {

        UserDetailsServiceResponse userDetails = userDetailsClient.getSkillsAndCareerPaths(userId);

        if(userDetails.getSkills().isEmpty()){
            logger.info("user : {} skills are empty", userId);
            throw new Exception("user skills Empty");
        }

        logger.info("fetched user : {} skills", userId);

        if(!userDetails.getCareerPaths().isEmpty()){
            logger.info("Getting Recommendations for userId : {} from DB", userId);
            return getRecommendationsFromDB(userDetails.getCareerPaths());
        }
        else{
            logger.info("Fetching Recommendations for userId : {} with AI", userId);
            return getRecommendationsFromAI(userId, userDetails.getSkills());
        }
    }

//    @CachePut(value = "career_recommendations", key = "#userId")
    public CareerPathResponse regenerateRecommendations(int userId) throws Exception {
        System.out.println("Regenerating Recommendations based on updated skills for userId : " + userId);
        UserDetailsServiceResponse userDetails = userDetailsClient.getSkillsAndCareerPaths(userId);

        if(userDetails.getSkills().isEmpty()){
            throw new Exception("user skills Empty");
        }
        return getRecommendationsFromAI(userId, userDetails.getSkills());
    }

    @CachePut(value = "user_recommendations_from_DB", key = "#skills")
    private CareerPathResponse getRecommendationsFromDB(List<Integer> skills) {

        List<CareerPath> roles = new ArrayList<>();

        for (int roleId : skills) {
            repo.findById(roleId).ifPresent(role -> {
                CareerPath cleanRole = (CareerPath) org.hibernate.Hibernate.unproxy(role);
                if (cleanRole.getSkillsRequired() != null) {
                    cleanRole.getSkillsRequired().size();
                }
                roles.add(cleanRole);
            });
        }

//        System.out.println(roles);
        return new CareerPathResponse(roles);
    }

    private CareerPathResponse getRecommendationsFromAI(int userId, List<String> skills) throws Exception {

        Set<String> notInDB = new HashSet<>(skills);

        List<CareerPath> inDB = repo.getCareerPathsInDB(skills);
        inDB.forEach(notInDB::remove);

        List<CareerPath> careerPaths = new ArrayList<>(inDB);

        System.out.println(notInDB.size());

        if(!notInDB.isEmpty()) {
            logger.info("forwarding request to ai-service");
            AiServiceResponse response = aiClient.generateRecommendations(new GenerateCareerPathRequest(notInDB));
            careerPaths.addAll(response.getRoles());
            repo.saveAll(response.getRoles());
        }

        System.out.println(careerPaths);
        List<Integer> careerPathIds = new ArrayList<>(careerPaths.stream()
                .map(CareerPath::getId)
                .toList());

        userDetailsClient.updateCareerPaths(userId, careerPathIds);

        List<CareerPath> cleanCareerPaths = new ArrayList<>();
        for (CareerPath cp : careerPaths) {
            CareerPath cleanCp = (CareerPath) org.hibernate.Hibernate.unproxy(cp);
            if (cleanCp.getSkillsRequired() != null) {
                cleanCp.getSkillsRequired().size();
            }
            cleanCareerPaths.add(cleanCp);
        }

        return new CareerPathResponse(cleanCareerPaths);
    }

    public List<CareerPath> searchCareerPaths(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword);
    }
}

