package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyRepository facultyRepository;


    @Test
    void shouldCreateFaculty() {
        Faculty faculty = new Faculty("gryf", "red");

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculties", faculty, Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(actualFaculty.getName(),faculty.getName());
        assertEquals(actualFaculty.getColor(),faculty.getColor());
    }

    @Test
    void shouldUpdateFaculty() {
        Faculty faculty = new Faculty("slyth", "blue");
        faculty = facultyRepository.save(faculty);
        Faculty updatedFaculty = new Faculty("name", "color");

        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(updatedFaculty);
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + faculty.getId(),
                HttpMethod.PUT,
                entity,
                Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(),faculty.getId());
        assertEquals(actualFaculty.getColor(),updatedFaculty.getColor());
        assertEquals(actualFaculty.getName(),updatedFaculty.getName());
    }

    @Test
    void sholudGetFaculty() {
        Faculty faculty = new Faculty("slyth", "blue");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + faculty.getId(),
                Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(),faculty.getId());
        assertEquals(actualFaculty.getColor(),faculty.getColor());
        assertEquals(actualFaculty.getName(),faculty.getName());
    }

    @Test
    void shouldDeleteFaculty() {
        Faculty faculty = new Faculty("slyth", "blue");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Assertions.assertThat(facultyRepository.findById(faculty.getId())).isNotPresent();
    }

    @Test
    void shouldGetByColor() {
    }

}


