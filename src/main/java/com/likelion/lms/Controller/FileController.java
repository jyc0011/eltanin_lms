package com.likelion.lms.Controller;

import com.likelion.lms.Domain.*;
import com.likelion.lms.Repository.UserHomeworkRepository;
import com.likelion.lms.Service.FileService;
import com.likelion.lms.Service.HomeworkService;
import com.likelion.lms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserHomeworkRepository userHomeworkRepository;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private UserService userService;

    @PostMapping("/upload/{id}")
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file, @PathVariable("id") Long homeworkId, HttpSession session){
        System.out.println("세션에서 가져온 User ID: " + session.getAttribute("id"));
        User user = userService.findById((Long)session.getAttribute("id"));
        Homework homework = homeworkService.getHomeworkById(homeworkId);
        String filePath = fileService.store(file);
        String fileDownloadUri = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", filePath).build().toString();
        UserHomework userHomework = new UserHomework();
        userHomework.setUser(user);
        userHomework.setHomework(homework);
        userHomework.setDateTime(LocalDateTime.now());
        File submittedFile = new File();
        submittedFile.setFileName(file.getOriginalFilename());
        submittedFile.setFilePath(filePath);
        submittedFile.setUserHomework(userHomework);
        userHomework.setFiles(List.of(submittedFile));
        userHomeworkRepository.save(userHomework);

        Map<String, String> response = new HashMap<>();
        response.put("fileName", file.getOriginalFilename());
        response.put("fileDownloadUri", fileDownloadUri);
        response.put("filePath", filePath);

        return response;
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource resource = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/deleteFile/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            fileService.deleteFileById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}