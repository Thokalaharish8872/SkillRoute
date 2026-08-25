package com.example.user_details_service.controller;

import com.example.user_details_service.entity.Profile;
import com.example.user_details_service.entity.Response.ProfileResponse;
import com.example.user_details_service.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/profile")
public class ProfileController {

    @Autowired
    ProfileService service;

    @GetMapping("/get_profile")
    public ProfileResponse getProfile(@RequestParam int userId) throws Exception {
        return service.getProfile(userId);
    }

    @PostMapping("/update_profile")
    public void updateProfile(@RequestBody Profile profile) throws Exception {
        service.updateProfile(profile);
    }

}


