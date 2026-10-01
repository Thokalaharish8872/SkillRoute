package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import com.example.skillroute_api_gateway.dto.DashboardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dashboard-service", configuration = FeignConfiguration.class)
public interface DashboardClient {

    @GetMapping("/api/dashboard/get_dashboard_data")
    DashboardResponse getDashboardData(@RequestParam(value = "userId", required = false) Integer userId);
}
