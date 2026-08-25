package com.example.user_details_service.service;

import com.example.user_details_service.dto.UpdateCareerPathsRequest;
import com.example.user_details_service.dto.UserDetailsResponse;
import com.example.user_details_service.entity.UserDetails;
import com.example.user_details_service.repository.UserDetailsRepo;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    private final UserDetailsRepo repo;

    public UserDetailsService(UserDetailsRepo repo) {
        this.repo = repo;
    }

    public UserDetailsResponse getSkillsAndCareerPaths(int userId) {

        UserDetails user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailsResponse(
                user.getSkills(),
                user.getCareerPaths()
        );
    }

    public void updateCareerPaths(int userId, UpdateCareerPathsRequest request) {

        UserDetails user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setCareerPaths(request.getCareerPaths());

        repo.save(user);
    }
}
