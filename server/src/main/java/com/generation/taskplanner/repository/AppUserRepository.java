package com.generation.taskplanner.repository;

import com.generation.taskplanner.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByClerkUserId(String clerkUserId);
}
