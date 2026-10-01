package com.example.dashboard_service.controllers;

import com.example.dashboard_service.dto.DashboardResponse;
import com.example.dashboard_service.security.JwtService;
import com.example.dashboard_service.services.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService service;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/get_dashboard_data")
    public DashboardResponse getDashboardData(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "userId", required = false) Integer userId) {

        if ((userId == null || userId == 0) && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }

        logger.info("Controller received get_dashboard_data for userId: {}", userId);
        return service.getDashboardData(userId);
    }
}
