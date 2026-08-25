package com.example.activity_service.repositories;

import com.example.activity_service.models.Activity;
import com.example.activity_service.models.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepo extends JpaRepository<UserActivity, Integer> {
}

