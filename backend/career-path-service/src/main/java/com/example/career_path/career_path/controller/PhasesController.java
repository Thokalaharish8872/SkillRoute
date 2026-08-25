package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.entity.Phases;
import com.example.career_path.career_path.entity.Response.PhasesResponse;
import com.example.career_path.career_path.service.PhasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/phases")
public class PhasesController {

    @Autowired
    PhasesService service;

    @GetMapping("get_phase")
    public PhasesResponse getPhase(@RequestParam int moduleId){
        return service.getPhase(moduleId);
    }
}


