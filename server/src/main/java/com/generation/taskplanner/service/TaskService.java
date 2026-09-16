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

    public List<Task> obtenerTodas() {
        return taskRepository.findAll();
    }

    public Optional<Task> obtenerPorId(Long id) {
        return taskRepository.findById(id);
    }

    public Task guardar(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> actualizar(Long id, Task task) {
        return taskRepository.findById(id).map(existente -> {
            existente.setName(task.getName());
            existente.setDescription(task.getDescription());
            existente.setDueDate(task.getDueDate());
            existente.setStatus(task.getStatus());
            return taskRepository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }
}
