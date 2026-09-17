package com.generation.taskplanner.controller;

import com.generation.taskplanner.support.TestJwtConfig;
import com.generation.taskplanner.support.TestJwtFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestJwtConfig.class)
class TaskApiAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userOnlySeesOwnTasks() throws Exception {
        String alice = TestJwtFactory.token("user_alice");
        String bob = TestJwtFactory.token("user_bob");

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + alice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Tarea de Alice","description":"privada","dueDate":"2026-09-20","status":"PENDING"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tarea de Alice"))
                .andExpect(jsonPath("$.clerkUserId").doesNotExist());

        mockMvc.perform(get("/api/tasks").header("Authorization", "Bearer " + bob))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(get("/api/tasks").header("Authorization", "Bearer " + alice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Tarea de Alice"));
    }

    @Test
    void userCannotUpdateOrDeleteAnotherUsersTask() throws Exception {
        String alice = TestJwtFactory.token("user_alice_2");
        String bob = TestJwtFactory.token("user_bob_2");

        MvcResult created = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + alice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Solo Alice","description":"no compartir","dueDate":"2026-09-21","status":"PENDING"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String id = com.jayway.jsonpath.JsonPath.read(created.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(put("/api/tasks/" + id)
                        .header("Authorization", "Bearer " + bob)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Hack","description":"no","dueDate":"2026-09-21","status":"DONE"}
                                """))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/tasks/" + id).header("Authorization", "Bearer " + bob))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/tasks/" + id)
                        .header("Authorization", "Bearer " + alice)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Solo Alice","description":"actualizada","dueDate":"2026-09-21","status":"DONE"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }
}
