package com.example.activity_service.mappers;

import com.example.activity_service.models.Response.UserActivityResponse;
import com.example.activity_service.models.UserActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserActivityResponse toDto(UserActivity user);

    UserActivity toEntity(UserActivityResponse dto);
}