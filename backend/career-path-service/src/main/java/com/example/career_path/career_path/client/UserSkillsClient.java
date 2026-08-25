package com.example.career_path.career_path.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("user-skills-service")
public interface UserSkillsClient {

    @PostMapping("get_user_skills")
    List<String> getUserSkills(int userId) throws Exception;
}

