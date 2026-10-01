package com.example.auth_service.mapper;

import com.example.auth_service.dto.request.RegisterRequestDto;
import com.example.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "streak", ignore = true)
    @Mapping(target = "roadmapProgress", ignore = true)
    @Mapping(target = "skillsLearned", ignore = true)
    @Mapping(target = "assessmentCompleted", ignore = true)
    @Mapping(target = "careerPaths", ignore = true)
    @Mapping(target = "profile", ignore = true)
    User toEntity(RegisterRequestDto request);
}
