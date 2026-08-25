package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import com.example.skillroute_api_gateway.dto.request.AddSkillRequest;
import com.example.skillroute_api_gateway.dto.request.RemoveSkillRequest;
import com.example.skillroute_api_gateway.dto.response.AddSkillResponse;
import com.example.skillroute_api_gateway.dto.response.GetSkillsResponse;
import com.example.skillroute_api_gateway.dto.response.ProfileResponse;
import com.example.skillroute_api_gateway.dto.response.RemoveSkillsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-details-service", configuration = FeignConfiguration.class)
public interface UserDetailsClient {

    @PostMapping("/api/skills/add_skill")
    AddSkillResponse addSkill(@RequestBody AddSkillRequest request);

    @GetMapping("/api/skills/get_user_skills")
    GetSkillsResponse getUserSkills(@RequestParam("userId") int userId);

    @DeleteMapping("/api/skills/remove_skill")
    RemoveSkillsResponse removeUserSkill(@RequestBody RemoveSkillRequest request);

    @GetMapping("/api/profile/get_profile")
    ProfileResponse getProfile(@RequestParam("userId") int userId);
}
