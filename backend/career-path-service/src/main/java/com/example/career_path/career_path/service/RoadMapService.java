package com.example.career_path.career_path.service;

import com.example.career_path.career_path.client.AIClient;
import com.example.career_path.career_path.dto.GenerateRoadMapRequest;
import com.example.career_path.career_path.repository.RoadMapRepo;
import com.example.career_path.career_path.entity.Response.RoadMapResponse;
import com.example.career_path.career_path.entity.RoadMap;
import com.example.career_path.career_path.entity.requests.RoadMapRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoadMapService {

    private static final Logger logger = LoggerFactory.getLogger(RoadMapService.class);

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

    public RoadMapResponse getRoadMap(String roleTitle) throws Exception {
        logger.info("getting roadmap with roleTitle {} for user", roleTitle);

        RoadMap roadMap = roadMapRepo.findByTitle(roleTitle);
        if(roadMap != null) {
            logger.info("RoadMap found in DB");
            return new RoadMapResponse(roadMap.getId(),
                    roadMap.getTitle(),
                    roadMap.getDescription(),
                    roadMap.getTotalPhases(),
                    roadMap.getPhases());
        }

        logger.info("Generating RoadMap with AI");
        RoadMapResponse aiResponse = aiClient.generateRoadMap(new GenerateRoadMapRequest(roleTitle));

        logger.info("response : {}", aiResponse);

        if (aiResponse == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        RoadMap generated = mapper.convertValue(aiResponse, RoadMap.class);
        RoadMap saved = roadMapRepo.save(generated);

        // Return from the saved entity so real DB-generated IDs (phases, modules) flow back to frontend
        return new RoadMapResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getTotalPhases(),
                saved.getPhases()
        );
    }
}
