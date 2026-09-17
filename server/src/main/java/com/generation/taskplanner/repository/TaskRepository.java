package com.generation.taskplanner.repository;

import com.generation.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByClerkUserId(String clerkUserId);

    Optional<Task> findByIdAndClerkUserId(Long id, String clerkUserId);

    boolean existsByIdAndClerkUserId(Long id, String clerkUserId);
}
