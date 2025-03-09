package com.internship.portal.service;

import com.internship.portal.exception.CustomException;
import com.internship.portal.mapper.ResumeMapper;
import com.internship.portal.model.entity.Resume;
import com.internship.portal.model.entity.User;
import com.internship.portal.model.resource.ResumeResource;
import com.internship.portal.repository.ResumeRepository;
import com.internship.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final AuthService authService;

    public ResumeService(
            ResumeRepository resumeRepository, UserRepository userRepository, ResumeMapper resumeMapper, AuthService authService) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.resumeMapper = resumeMapper;
        this.authService = authService;
    }

    public ResumeResource uploadResume(MultipartFile file) throws IOException{
        if (file.isEmpty()) {
            throw new CustomException("File cannot be empty", HttpStatus.BAD_REQUEST);
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Long loggedInUserId = authService.getLoggedInUserId();
        String fileName = loggedInUserId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Resume resume = resumeRepository.findByJobSeekerId(loggedInUserId);
        if (resume != null) {
            resume.setFilePath(filePath.toString());
        } else {
            resume = new Resume();
            resume.setJobSeeker(userRepository.findById(loggedInUserId).get());
            resume.setFilePath(filePath.toString());
        }
        Resume savedResume = resumeRepository.save(resume);
        return resumeMapper.toResource(savedResume);
    }
}
