package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
         return studentRepository.save(student);
    }

    @Override
    public Student get(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student update(Long id,Student student) {
       return studentRepository.findById(id).map(studentFromDb -> {
            studentFromDb.setName(student.getName());
            studentFromDb.setAge(student.getAge());
            return studentRepository.save(student);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getByAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Override
    public List<Student> getByAgeBetween(int ageFrom, int ageTo) {
        return studentRepository.findByAgeBetween(ageFrom,ageTo);
    }

    @Override
    public Faculty getFaculty(Long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElse(null);
    }

    @Override
    public int getStudentCount() {
        return studentRepository.getStudentCount();
    }

    @Override
    public int getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> getLastFive() {
        return studentRepository.getLastFive();
    }
}
