package com.example.activity_service.controllers;

import com.example.activity_service.models.requests.ProgressUpdateRequest;
import com.example.activity_service.models.Response.ProgressResponse;
import com.example.activity_service.services.ProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/progress")
public class ProgressController {
    private static final Logger logger = LoggerFactory.getLogger(ProgressController.class);

    @Autowired
    ProgressService service;

    @Autowired
    com.example.activity_service.security.JwtService jwtService;

    @PostMapping("/update_active_time")
    public void updateActiveTime(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ProgressUpdateRequest request){
        if ((request.getUserId() == 0) && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                request.setUserId(extracted);
            }
        }
        logger.info("received update_activity_time from userId : {}", request.getUserId());
        service.updateActiveTime(request);
    }

    @GetMapping("/get_progress")
    public ProgressResponse getProgress(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId) throws Exception{
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("received get_progress from userId : {}", userId);
        return service.getProgress(userId);
    }
}

