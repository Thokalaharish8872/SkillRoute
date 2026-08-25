package com.example.career_path.career_path.entity.Response;

import com.example.career_path.career_path.entity.Module;
import com.example.career_path.career_path.entity.Skills;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleResponse {
    
    private Module module;
    private List<Skills> skills;
}


