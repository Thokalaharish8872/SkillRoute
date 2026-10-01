package com.example.activity_service.models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse implements Serializable {

    private boolean isFirstTask;
    private int streak;
}

