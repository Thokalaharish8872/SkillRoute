package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "search-service", configuration = FeignConfiguration.class)
public interface SearchClient {

    @GetMapping("/api/search/search")
    Object search(@RequestParam("keyword") String keyword);
}
