package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
@Service
public class AvatarServiceImpl implements AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("method uploadAvatar was invoke");
        Student student = studentRepository.findById(studentId).orElseThrow();

        Path path = saveToDisk(student, avatarFile);
        saveToDb(student,avatarFile,path);
    }

    @Override
    public Avatar findAvatar(Long id) {
        logger.info("method findAvatar was invoke");
        return avatarRepository.findById(id).orElse(null);
    }

    @Override
    public List<Avatar> getPaginatedAvatars(int pageNumber, int pageSize) {
        logger.info("method getPaginatedAvatars was invoke");
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private String getExtensions(String fileName) {
        logger.info("method getExtensions was invoke");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private Avatar findAvatarByStudentId(Long studentId) {
        logger.info("method findAvatarByStudentId was invoke");
        return avatarRepository.findByStudent_id(studentId).orElse(new Avatar());
    }

    private Path saveToDisk(Student student, MultipartFile avatarFile ) throws IOException {
        logger.info("method saveToDisk was invoke");
        Path filePath = Path.of(avatarsDir, "student"+ student.getId() + "avatar." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        return filePath;
    }

    private void saveToDb(Student student,MultipartFile avatarFile, Path filePath) throws IOException {
        logger.info("method saveToDb was invoke");
        Avatar avatar = findAvatarByStudentId(student.getId());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }
}
