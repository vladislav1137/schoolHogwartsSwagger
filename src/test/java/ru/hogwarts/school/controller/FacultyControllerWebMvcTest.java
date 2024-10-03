package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FacultyService facultyService;
    @MockBean
    private AvatarService avatarService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void shouldCreateFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("gryf", "red");
        Faculty savedFaculty = new Faculty("gryf", "red");
        savedFaculty.setId(facultyId);

        when(facultyService.add(faculty)).thenReturn(savedFaculty);

        ResultActions perform = mockMvc.perform(post("/faculties").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));
        perform
                .andDo(print())
                .andExpect(jsonPath("$.id").value(savedFaculty.getId()))
                .andExpect(jsonPath("$.name").value(savedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(savedFaculty.getColor()));
    }

    @Test
    void shouldUpdateFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("gryf", "red");

        when(facultyService.update(facultyId,faculty)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(put("/faculties/{id}",facultyId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));
        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void shouldDeleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("gryf", "red");

        when(facultyService.get(facultyId)).thenReturn(faculty);
        mockMvc.perform(get("/faculties/{id}", facultyId))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void shouldGetFacultyByColor() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty("gryf", "red");

        when(facultyService.getByColor(faculty.getColor())).thenReturn(Collections.singletonList(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties?color=red")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll();
    }
}
