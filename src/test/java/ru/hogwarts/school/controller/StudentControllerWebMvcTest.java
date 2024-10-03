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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudentService studentService;
    @MockBean
    private AvatarService avatarService;
    @Autowired
    private WebApplicationContext webApplicationContext;



    @Test
    void shouldCreateStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Kostya", 14);
        Student savedStudent = new Student("Kostya", 14);
        savedStudent.setId(studentId);

        when(studentService.add(student)).thenReturn(savedStudent);

        ResultActions perform = mockMvc.perform(post("/student").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
                perform
                        .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                        .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                        .andExpect(jsonPath("$.age").value(savedStudent.getAge()));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Nikita", 16);

        when(studentService.update(studentId,student)).thenReturn(student);

        ResultActions perform = mockMvc.perform(put("/student/{id}",studentId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student("Roman", 12);

        when(studentService.get(studentId)).thenReturn(student);
        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void shouldGetStudentByAge() throws Exception {
        Student student = new Student("Roman", 12);

        when(studentService.getByAge(student.getAge())).thenReturn(Collections.singletonList(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=12")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll();
    }
    }
