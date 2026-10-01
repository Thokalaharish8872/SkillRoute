package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.entity.Module;
import com.example.career_path.career_path.entity.Response.ModuleResponse;
import com.example.career_path.career_path.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/modules")
public class ModuleController {

    @Autowired
    ModuleService service;

    private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @GetMapping("/get_module")
    public ModuleResponse getModule(@RequestParam int moduleId) throws Exception {
        logger.info("received get_module request for moduleId : {}", moduleId);
        ModuleResponse response = service.getModule(moduleId);
        logger.info("returning response for get_module : {}", response);
        return response;
    }

    @GetMapping("/search")
    public java.util.List<Module> searchModules(@RequestParam String keyword) {
        return service.searchModules(keyword);
    }
}


