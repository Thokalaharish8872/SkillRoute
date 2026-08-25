package com.example.search_service.services;

import com.example.search_service.client.CareerPathClient;
import com.example.search_service.models.Response.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private CareerPathClient careerPathClient;

    public List<SearchResponse> search(String keyword) {
        List<SearchResponse> responses = new ArrayList<>();

        responses.add(new SearchResponse("career path", careerPathClient.searchCareerPaths(keyword)));
        responses.add(new SearchResponse("module", careerPathClient.searchModules(keyword)));
        responses.add(new SearchResponse("skill", careerPathClient.searchSkills(keyword)));

        return responses;
    }
}
