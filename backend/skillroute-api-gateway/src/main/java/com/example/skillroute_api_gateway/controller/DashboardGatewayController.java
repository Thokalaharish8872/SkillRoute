package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.client.DashboardClient;
import com.example.skillroute_api_gateway.dto.DashboardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DashboardGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardGatewayController.class);

    @Autowired
    private DashboardClient dashboardClient;

    @GetMapping("/api/dashboard/get_dashboard_data")
    public DashboardResponse getDashboardData(@RequestParam(required = false) Integer userId) {
        logger.info("Gateway received get_dashboard_data request for userId: {}", userId);
        return dashboardClient.getDashboardData(userId);
    }
}
