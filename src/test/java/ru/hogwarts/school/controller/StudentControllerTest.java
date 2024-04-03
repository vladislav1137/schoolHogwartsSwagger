package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    FacultyRepository facultyRepository;

    @Test
    void shouldCreateStudent() {
        Student student = new Student("Ivan",19);

        ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/student", student, Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Student actualStudent = studentResponseEntity.getBody();
        assertNotNull(actualStudent.getId());
        assertEquals(actualStudent.getName(),student.getName());
        assertEquals(actualStudent.getAge(),student.getAge());
    }
    @Test
    void shouldUpdateStudent() {
        Student student = new Student("Nikita", 18);
        student = studentRepository.save(student);
        Student updatedStudent =new Student("Nikita Kologriviy", 20);

        HttpEntity<Student> entity = new HttpEntity<Student>(updatedStudent);
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + student.getId(),
                HttpMethod.PUT,
                entity,
                Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertEquals(actualStudent.getAge(),updatedStudent.getAge());
        assertEquals(actualStudent.getName(),updatedStudent.getName());
    }
    @Test
    void sholudGetStudent() {
        Student student = new Student("Robert", 14);
        student = studentRepository.save(student);

        ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + student.getId(),
                Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertEquals(actualStudent.getId(),student.getId());
        assertEquals(actualStudent.getAge(),student.getAge());
        assertEquals(actualStudent.getName(),student.getName());
    }
    @Test
    void shouldDeleteStudent() {
        Student student = new Student("Petr", 16);
        student = studentRepository.save(student);

        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + student.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Assertions.assertThat(studentRepository.findById(student.getId())).isNotPresent();
    }

    @Test
    void shouldGetByAge() {
        Student student = new Student("Julia", 17);
        student = studentRepository.save(student);

        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student?age=" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});


        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> actualStudent = studentResponseEntity.getBody();
    }

    @Test
    public void shouldGetByAgeBetween() {

        Student student = new Student("Roman", 21);
        student = studentRepository.save(student);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/getByAgeBetween?ageFrom=20" + "&ageTo=22",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });
        List<Student> students = response.getBody();

        assertNotNull(students);
        for (Student student1 : students) {
            assertTrue(student.getAge() >= 20 && student.getAge() <= 22);
        }
    }

    @Test
    void sholudGetFacultyByStudent() {
        Student student = new Student("Sam", 13);
        student = studentRepository.save(student);
        Faculty faculty = new Faculty("slyth", "blue");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/student/" + student.getId()+"/faculty",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> actualStudent = studentResponseEntity.getBody();

    }
}
