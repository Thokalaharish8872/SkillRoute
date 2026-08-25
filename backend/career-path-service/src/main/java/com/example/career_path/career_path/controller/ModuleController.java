package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.entity.Module;
import com.example.career_path.career_path.entity.Response.ModuleResponse;
import com.example.career_path.career_path.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/modules")
public class ModuleController {

    @Autowired
    ModuleService service;

    @GetMapping("/get_module")
    public ModuleResponse getModule(@RequestParam int moduleId) throws Exception {
        return service.getModule(moduleId);
    }

    @GetMapping("/search")
    public java.util.List<Module> searchModules(@RequestParam String keyword) {
        return service.searchModules(keyword);
    }
}


