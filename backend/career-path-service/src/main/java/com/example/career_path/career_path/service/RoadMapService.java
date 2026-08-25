package com.example.career_path.career_path.service;

import com.example.career_path.career_path.client.AIClient;
import com.example.career_path.career_path.dto.GenerateRoadMapRequest;
import com.example.career_path.career_path.repository.RoadMapRepo;
import com.example.career_path.career_path.entity.Response.RoadMapResponse;
import com.example.career_path.career_path.entity.RoadMap;
import com.example.career_path.career_path.entity.requests.RoadMapRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoadMapService {

    @Autowired
    private RoadMapRepo roadMapRepo;

    @Autowired
    private PhasesService phaseService;

    @Autowired
    private AIClient aiClient;

    public RoadMap addRoadMap(RoadMap request) {
        roadMapRepo.save(request);
        return request;
    }

    public RoadMapResponse getRoadMap(RoadMapRequest request) throws Exception {
        System.out.println("Getting RoadMap for user");

        RoadMap roadMap = roadMapRepo.findByTitle(request.getRoleTitle());
        System.out.println(request.getRoleTitle());
        if(roadMap != null) {
            System.out.println("RoadMap found in DB");
            return new RoadMapResponse(roadMap.getId(),
                    roadMap.getTitle(),
                    roadMap.getDescription(),
                    roadMap.getTotalPhases(),
                    roadMap.getPhases());
        }

        System.out.println("Generating RoadMap with AI");
        Object raw = aiClient.generateRoadMap(new GenerateRoadMapRequest(request.getRoleTitle()));
        ObjectMapper mapper = new ObjectMapper();
        RoadMap generated = mapper.convertValue(raw, RoadMap.class);
        RoadMap response = roadMapRepo.save(generated);

        return new RoadMapResponse(response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getTotalPhases(),
                response.getPhases());
    }
}
