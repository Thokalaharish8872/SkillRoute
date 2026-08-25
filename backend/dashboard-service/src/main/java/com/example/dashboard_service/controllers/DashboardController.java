package com.example.dashboard_service.controllers;

import com.example.dashboard_service.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService service;


}

