package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.entity.RoadMap;
import com.example.career_path.career_path.entity.Response.RoadMapResponse;
import com.example.career_path.career_path.entity.requests.RoadMapRequest;
import com.example.career_path.career_path.service.RoadMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/roadmap")
public class RoadMapController {

    @Autowired
    private RoadMapService service;

    @PostMapping("/add_roadmap")
    public RoadMap addRoadMap(@RequestBody RoadMap request){
        return service.addRoadMap(request);
    }

    @PostMapping("/get_roadmap")
    public RoadMapResponse getRoadMap(@RequestBody RoadMapRequest request) throws Exception {
        return service.getRoadMap(request);
    }
}
