package com.generation.taskplanner.service;

import com.generation.taskplanner.model.Task;
import com.generation.taskplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> obtenerTodas(String clerkUserId) {
        return taskRepository.findAllByClerkUserId(clerkUserId);
    }

    public Task guardarParaUsuario(Task task, String clerkUserId) {
        task.setId(null);
        task.setClerkUserId(clerkUserId);
        return taskRepository.save(task);
    }

    public Optional<Task> actualizarParaUsuario(Long id, Task task, String clerkUserId) {
        return taskRepository.findByIdAndClerkUserId(id, clerkUserId).map(existente -> {
            existente.setName(task.getName());
            existente.setDescription(task.getDescription());
            existente.setDueDate(task.getDueDate());
            existente.setStatus(task.getStatus());
            return taskRepository.save(existente);
        });
    }

    public boolean eliminarParaUsuario(Long id, String clerkUserId) {
        if (!taskRepository.existsByIdAndClerkUserId(id, clerkUserId)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }
}
