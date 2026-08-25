package com.example.activity_service.controllers;

import com.example.activity_service.services.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/streak/")
@CrossOrigin
public class StreakController {

    @Autowired
    StreakService service;

    @PostMapping("/update_streak")
    public void updateSteak(@RequestParam int userId){
        service.updateStreak(userId);
    }

    @GetMapping("/get_streak")
    public int getStreak(@RequestParam int userId){
        return service.getStreak(userId);
    }
}

