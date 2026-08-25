package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.client.SearchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/search")
public class SearchGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(SearchGatewayController.class);

    @Autowired
    private SearchClient searchClient;

    @GetMapping("/search")
    public Object search(@RequestParam String keyword) {
        logger.info("Received search request for keyword: {}", keyword);
        return searchClient.search(keyword);
    }
}
