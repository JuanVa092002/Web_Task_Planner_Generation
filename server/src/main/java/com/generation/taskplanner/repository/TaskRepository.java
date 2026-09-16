package com.generation.taskplanner.repository;

import com.generation.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
