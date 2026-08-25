package com.example.auth_service.mapper;

import com.example.auth_service.dto.request.RegisterRequestDto;
import com.example.auth_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequestDto request);
}
