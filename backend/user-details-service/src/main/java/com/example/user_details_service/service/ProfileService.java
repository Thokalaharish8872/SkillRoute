package com.example.user_details_service.service;

import com.example.user_details_service.repository.ProfileRepo;
import com.example.user_details_service.entity.Profile;
import com.example.user_details_service.entity.Response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    ProfileRepo repo;

    public ProfileResponse getProfile(int userId) throws Exception {

        Profile profile = repo.findByUserId(userId);
        if(profile == null){
            throw new Exception("profile not found");
        }

        return  new ProfileResponse(profile.getUserId(),
                                    profile.getUserName(),
                                    profile.getEmail(),
                                    profile.getRole(),
                                    profile.getLocation(),
                                    profile.getCodingProfiles());
    }

    public void updateProfile(Profile profile) throws Exception {
        Profile profile1 = repo.findByUserId(profile.getUserId());

        if(profile1 == null){
            createProfile(profile);
            return;
        }

        if(profile.getUserName() != null)
            profile1.setUserName(profile.getUserName());
        if(profile.getEmail() != null)
            profile1.setEmail(profile.getEmail());
        if(profile.getRole() != null)
            profile1.setRole(profile.getRole());
        if(profile.getLocation() != null)
            profile1.setLocation(profile.getLocation());
        if(profile.getCodingProfiles() != null)
            profile1.setCodingProfiles(profile.getCodingProfiles());

        repo.save(profile1);
    }

    public void createProfile(Profile profile){
        repo.save(profile);
    }
}


