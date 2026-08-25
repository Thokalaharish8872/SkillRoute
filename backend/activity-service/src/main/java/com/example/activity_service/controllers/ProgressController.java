package com.example.activity_service.controllers;

import com.example.activity_service.models.requests.ProgressUpdateRequest;
import com.example.activity_service.models.Response.ProgressResponse;
import com.example.activity_service.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    ProgressService service;

    @PostMapping("/update_active_time")
    public void updateActiveTime(@RequestBody ProgressUpdateRequest request){
        System.out.println("called");
        service.updateActiveTime(request);
    }

    @GetMapping("/get_progress")
    public ProgressResponse getProgress(@RequestParam int userId){
        return service.getProgress(userId);
    }
}

