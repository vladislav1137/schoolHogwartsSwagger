package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
         logger.info("method add was invoke");
        return studentRepository.save(student);
    }

    @Override
    public Student get(Long id) {
        logger.info("method get was invoke");
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student update(Long id,Student student) {
        logger.info("method update was invoke");
       return studentRepository.findById(id).map(studentFromDb -> {
            studentFromDb.setName(student.getName());
            studentFromDb.setAge(student.getAge());
            return studentRepository.save(student);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        logger.info("method delete was invoke");
        studentRepository.deleteById(id);
    }

    public List<Student> getByAge(int age) {
        logger.info("method getByAge was invoke");
        return studentRepository.findByAge(age);
    }

    @Override
    public List<Student> getByAgeBetween(int ageFrom, int ageTo) {
        logger.info("method getByAgeBetween was invoke");
        return studentRepository.findByAgeBetween(ageFrom,ageTo);
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("method getFaculty was invoke");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElse(null);
    }

    @Override
    public int getStudentCount() {
        logger.info("method getStudentCount was invoke");
        return studentRepository.getStudentCount();
    }

    @Override
    public int getAverageAge() {
        logger.info("method getAverageAge was invoke");
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> getLastFive() {
        logger.info("method getLastFive was invoke");
        return studentRepository.getLastFive();
    }
}
