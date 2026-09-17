package com.generation.taskplanner.controller;

import com.generation.taskplanner.model.Task;
import com.generation.taskplanner.service.TaskService;
import com.generation.taskplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> listarTodas(@AuthenticationPrincipal Jwt jwt) {
        String clerkUserId = ensureUser(jwt);
        return ResponseEntity.ok(taskService.obtenerTodas(clerkUserId));
    }

    @PostMapping
    public ResponseEntity<Task> crear(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody Task task) {
        String clerkUserId = ensureUser(jwt);
        Task nuevaTarea = taskService.guardarParaUsuario(task, clerkUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> actualizar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody Task task
    ) {
        String clerkUserId = ensureUser(jwt);
        return taskService.actualizarParaUsuario(id, task, clerkUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        String clerkUserId = ensureUser(jwt);
        if (taskService.eliminarParaUsuario(id, clerkUserId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private String ensureUser(Jwt jwt) {
        String clerkUserId = jwt.getSubject();
        userService.ensureUser(clerkUserId, jwt.getClaimAsString("email"), jwt.getClaimAsString("name"));
        return clerkUserId;
    }
}
