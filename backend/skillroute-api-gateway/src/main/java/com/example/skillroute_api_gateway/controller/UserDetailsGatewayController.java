package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.client.UserDetailsClient;
import com.example.skillroute_api_gateway.dto.request.AddSkillRequest;
import com.example.skillroute_api_gateway.dto.request.RemoveSkillRequest;
import com.example.skillroute_api_gateway.dto.response.AddSkillResponse;
import com.example.skillroute_api_gateway.dto.response.GetSkillsResponse;
import com.example.skillroute_api_gateway.dto.response.ProfileResponse;
import com.example.skillroute_api_gateway.dto.response.RemoveSkillsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserDetailsGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsGatewayController.class);

    @Autowired
    private UserDetailsClient userDetailsClient;

    @PostMapping("/api/skills/add_skill")
    public AddSkillResponse addSkill(@RequestBody AddSkillRequest request) {
        logger.info("Received add_skill request");
        return userDetailsClient.addSkill(request);
    }

    @GetMapping("/api/skills/get_user_skills")
    public GetSkillsResponse getUserSkills(@RequestParam int userId) {
        logger.info("Received get_user_skills request for userId: {}", userId);
        return userDetailsClient.getUserSkills(userId);
    }

    @DeleteMapping("/api/skills/remove_skill")
    public RemoveSkillsResponse removeUserSkill(@RequestBody RemoveSkillRequest request) {
        logger.info("Received remove_skill request");
        return userDetailsClient.removeUserSkill(request);
    }

    @GetMapping("/api/profile/get_profile")
    public ProfileResponse getProfile(@RequestParam int userId) {
        logger.info("Received get_profile request for userId: {}", userId);
        return userDetailsClient.getProfile(userId);
    }
}
