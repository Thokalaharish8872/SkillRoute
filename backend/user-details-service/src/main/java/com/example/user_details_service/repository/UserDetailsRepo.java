package com.example.user_details_service.repository;

import com.example.user_details_service.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<UserDetails, Integer> {
}
