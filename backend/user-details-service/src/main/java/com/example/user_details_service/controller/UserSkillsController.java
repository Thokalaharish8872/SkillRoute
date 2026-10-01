package com.example.user_details_service.controller;

import com.example.user_details_service.entity.Response.AddSkillResponse;
import com.example.user_details_service.entity.Response.GetSkillsResponse;
import com.example.user_details_service.entity.Response.RemoveSkillsResponse;
import com.example.user_details_service.entity.Response.UserSkillsResponse;
import com.example.user_details_service.entity.requests.AddSkillRequest;
import com.example.user_details_service.entity.requests.RemoveSkillRequest;
import com.example.user_details_service.service.UserSkillsService;
import org.hibernate.Remove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/skills")
public class UserSkillsController {

    private static final Logger logger = LoggerFactory.getLogger(UserSkillsController.class);

    @GetMapping("/health")
    public String health(){
        System.out.println("health check request came");
        return "Ok";
    }

    @Autowired
    UserSkillsService service;

    @Autowired
    com.example.user_details_service.security.JwtService jwtService;

    @PostMapping("/add_skill")
    public GetSkillsResponse addSkill(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody AddSkillRequest request) throws Exception {
        if (request.getUserId() == 0 && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                request.setUserId(extracted);
            }
        }
        logger.info("received request to add new skill to userId : {}", request.getUserId());
        return service.addSkill(request);
    }

    @GetMapping("/get_user_skills")
    public GetSkillsResponse getUserSkills(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId) throws Exception{
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("Received get_user_skills request for userId: {}", userId);
        return service.getUserSkills(userId);
    }

    @DeleteMapping("/remove_skill")
    public GetSkillsResponse removeUserSkill(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RemoveSkillRequest request){
        if (request.getUserId() == 0 && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                request.setUserId(extracted);
            }
        }
        logger.info("Received remove_skills request for userId: {}", request.getUserId());
        return service.removeUserSkill(request.getUserId(), request.getSkillName());
    }
}


