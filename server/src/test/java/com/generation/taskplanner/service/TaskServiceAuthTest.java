package com.generation.taskplanner.service;

import com.generation.taskplanner.model.AppUser;
import com.generation.taskplanner.model.Task;
import com.generation.taskplanner.repository.AppUserRepository;
import com.generation.taskplanner.repository.TaskRepository;
import com.generation.taskplanner.support.TestJwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(TestJwtConfig.class)
class TaskServiceAuthTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void guardarAsignaElUsuarioAutenticado() {
        Task task = new Task();
        task.setName("Estudiar Clerk");
        task.setDescription("TDD");
        task.setDueDate(LocalDate.parse("2026-09-22"));
        task.setStatus("PENDING");

        Task saved = taskService.guardarParaUsuario(task, "user_svc_1");

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getClerkUserId()).isEqualTo("user_svc_1");
        assertThat(taskRepository.findAllByClerkUserId("user_svc_1")).hasSize(1);
        assertThat(taskRepository.findAllByClerkUserId("otro")).isEmpty();
    }

    @Test
    void ensureUserCreaYNoDuplica() {
        AppUser first = userService.ensureUser("user_svc_2", "a@example.com", "Ana");
        AppUser second = userService.ensureUser("user_svc_2", "a@example.com", "Ana");

        assertThat(first.getId()).isEqualTo(second.getId());
        assertThat(appUserRepository.count()).isEqualTo(1);
        assertThat(first.getEmail()).isEqualTo("a@example.com");
    }

    @Test
    void actualizarYEliminarSoloPropias() {
        Task task = new Task();
        task.setName("Mia");
        task.setDueDate(LocalDate.parse("2026-09-22"));
        task.setStatus("PENDING");
        Task saved = taskService.guardarParaUsuario(task, "owner");

        Task update = new Task();
        update.setName("Mia");
        update.setDescription("done");
        update.setDueDate(LocalDate.parse("2026-09-22"));
        update.setStatus("DONE");

        assertThat(taskService.actualizarParaUsuario(saved.getId(), update, "intruso")).isEmpty();
        assertThat(taskService.eliminarParaUsuario(saved.getId(), "intruso")).isFalse();
        assertThat(taskService.actualizarParaUsuario(saved.getId(), update, "owner")).isPresent();
        assertThat(taskService.eliminarParaUsuario(saved.getId(), "owner")).isTrue();
        assertThat(taskService.obtenerTodas("owner")).isEmpty();
        List<Task> leftover = taskRepository.findAll();
        assertThat(leftover).isEmpty();
    }
}
