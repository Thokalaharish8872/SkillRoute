package com.example.career_path.career_path.client;

import com.example.career_path.career_path.dto.UserDetailsServiceResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsClientFallback implements UserDetailsClient {

    @Override
    public UserDetailsServiceResponse getSkillsAndCareerPaths(int userId) {
        System.err.println("Fallback triggered for UserDetailsClient.getSkillsAndCareerPaths for userId: " + userId);
        return new UserDetailsServiceResponse(new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public void updateCareerPaths(int userId, List<Integer> careerPaths) throws Exception {
        System.err.println("Fallback triggered for UserDetailsClient.updateCareerPaths for userId: " + userId);
    }

    @Override
    public String health() {
        return "User Details Service Unavailable (Fallback)";
    }
}
