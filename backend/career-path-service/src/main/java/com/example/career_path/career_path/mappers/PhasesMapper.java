package com.example.career_path.career_path.mappers;

import com.example.career_path.career_path.entity.Phases;
import com.example.career_path.career_path.entity.Response.PhasesResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhasesMapper {

    PhasesResponse toDto(Phases phases);
    Phases toEntity(PhasesResponse response);

}
