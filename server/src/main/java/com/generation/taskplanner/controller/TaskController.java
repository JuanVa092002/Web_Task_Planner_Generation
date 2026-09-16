package com.generation.taskplanner.controller;

import com.generation.taskplanner.model.Task;
import com.generation.taskplanner.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> listarTodas() {
        return ResponseEntity.ok(taskService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Task> crear(@Valid @RequestBody Task task) {
        Task nuevaTarea = taskService.guardar(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> actualizar(@PathVariable Long id, @Valid @RequestBody Task task) {
        return taskService.actualizar(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (taskService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
