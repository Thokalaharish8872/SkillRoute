package com.example.user_details_service.service;

import com.example.user_details_service.repository.ProfileRepo;
import com.example.user_details_service.entity.CodingProfiles;
import com.example.user_details_service.entity.Profile;
import com.example.user_details_service.entity.Response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    ProfileRepo repo;

    @Cacheable(value = "user_profile", key = "#userId")
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

    @CachePut(value = "user_profile", key = "#profile.getUserId()")
    public ProfileResponse updateProfile(Profile profile) throws Exception {
        Profile profile1 = repo.findByUserId(profile.getUserId());

        if(profile1 == null){
            return createProfile(profile);
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

        Profile saved = repo.save(profile1);
        return new ProfileResponse(saved.getUserId(),
                                    saved.getUserName(),
                                    saved.getEmail(),
                                    saved.getRole(),
                                    saved.getLocation(),
                                    saved.getCodingProfiles());
    }

    @CachePut(value = "user_profile", key = "#profile.getUserId()")
    public ProfileResponse createProfile(Profile profile) throws Exception {
        Profile existing = repo.findByUserId(profile.getUserId());
        if (existing != null) {
            return updateProfile(profile);
        }

        if (profile.getCodingProfiles() == null) {
            CodingProfiles defaultCoding = new CodingProfiles();
            defaultCoding.setLeetcode("");
            defaultCoding.setGithub("");
            defaultCoding.setCodechef("");
            defaultCoding.setCodeforces("");
            profile.setCodingProfiles(defaultCoding);
        }

        Profile saved = repo.save(profile);
        return new ProfileResponse(saved.getUserId(),
                                    saved.getUserName(),
                                    saved.getEmail(),
                                    saved.getRole(),
                                    saved.getLocation(),
                                    saved.getCodingProfiles());
    }
}


