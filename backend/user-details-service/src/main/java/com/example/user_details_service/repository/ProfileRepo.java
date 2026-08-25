package com.example.user_details_service.repository;

import com.example.user_details_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer> {
    Profile findByUserId(int userId);
}


