package com.example.auth_service.repository;

import com.example.auth_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer> {
    Profile findByUserId(Integer userId);
    Profile findByEmail(String email);
}
