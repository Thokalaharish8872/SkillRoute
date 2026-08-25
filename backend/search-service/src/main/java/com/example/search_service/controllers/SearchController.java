package com.example.search_service.controllers;

import com.example.search_service.models.Response.SearchResponse;
import com.example.search_service.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/search")
public class SearchController {

    @Autowired
    SearchService service;

    @GetMapping("/search")
    public List<SearchResponse> search(@RequestParam String keyword){
        return service.search(keyword);
    }
}

