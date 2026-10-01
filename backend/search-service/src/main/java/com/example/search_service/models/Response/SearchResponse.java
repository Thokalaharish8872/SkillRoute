package com.example.search_service.models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse implements Serializable {

    String resultType;
    List<?> result;
}

