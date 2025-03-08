package com.internship.portal.service;

import com.internship.portal.mapper.ResumeMapper;
import com.internship.portal.model.entity.Resume;
import com.internship.portal.model.entity.User;
import com.internship.portal.model.resource.ResumeResource;
import com.internship.portal.repository.ResumeRepository;
import com.internship.portal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class ResumeService {

    private static final String UPLOAD_DIR = "uploads/resumes/";
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ResumeMapper resumeMapper;


    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, ResumeMapper resumeMapper) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.resumeMapper = resumeMapper;
    }

    public ResumeResource uploadResume(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Resume resume = resumeRepository.findByJobSeekerId(userId);
        if (resume != null) {
            resume.setFilePath(filePath.toString());
        } else {
            resume = new Resume();
            resume.setJobSeeker(user);
            resume.setFilePath(filePath.toString());
        }
        Resume savedResume = resumeRepository.save(resume);
        return resumeMapper.toResource(savedResume);
    }
}
