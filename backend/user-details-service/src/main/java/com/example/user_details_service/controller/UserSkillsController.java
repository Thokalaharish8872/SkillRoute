package com.example.user_details_service.controller;

import com.example.user_details_service.entity.Response.AddSkillResponse;
import com.example.user_details_service.entity.Response.GetSkillsResponse;
import com.example.user_details_service.entity.Response.RemoveSkillsResponse;
import com.example.user_details_service.entity.Response.UserSkillsResponse;
import com.example.user_details_service.entity.requests.AddSkillRequest;
import com.example.user_details_service.entity.requests.RemoveSkillRequest;
import com.example.user_details_service.service.UserSkillsService;
import org.hibernate.Remove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/skills")
public class UserSkillsController {


    @GetMapping("/health")
    public String health(){
        System.out.println("health check request came");
        return "Ok";
    }

    @Autowired
    UserSkillsService service;

    @PostMapping("/add_skill")
    public AddSkillResponse addSkill(@RequestBody AddSkillRequest request) throws Exception {
        System.out.println("called");
        return service.addSkill(request);
    }

    @GetMapping("/get_user_skills")
    public GetSkillsResponse getUserSkills(@RequestParam int userId) throws Exception{
        return service.getUserSkills(userId);
    }

    @DeleteMapping("/remove_skill")
    public RemoveSkillsResponse removeUserSkill(@RequestBody RemoveSkillRequest request){
        return service.removeUserSkill(request.getUserId(), request.getSkillName());
    }
}


