package com.example.search_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("career-path-service")
public interface CareerPathClient {

    @GetMapping("/api/career_path/search")
    List<Object> searchCareerPaths(@RequestParam("keyword") String keyword);

    @GetMapping("/api/modules/search")
    List<Object> searchModules(@RequestParam("keyword") String keyword);

    @GetMapping("/api/skills/search")
    List<Object> searchSkills(@RequestParam("keyword") String keyword);
}
