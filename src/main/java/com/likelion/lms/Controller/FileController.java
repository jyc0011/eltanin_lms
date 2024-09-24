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

@RestController // RESTful API 컨트롤러임을 명시
public class FileController {

    @Autowired // FileService, UserHomeworkRepository, HomeworkService, UserService 의존성 자동 주입
    private FileService fileService;
    @Autowired
    private UserHomeworkRepository userHomeworkRepository;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private UserService userService;

    // 파일 업로드 처리
    // MultipartFile을 받아와 해당 파일을 저장하고, 업로드된 파일에 대한 URI를 생성하여 반환
    @PostMapping("/upload/{id}")
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file, @PathVariable("id") Long homeworkId, HttpSession session) {

        // 세션에서 사용자 ID를 가져와 확인
        System.out.println("세션에서 가져온 User ID: " + session.getAttribute("id"));
        User user = userService.findById((Long) session.getAttribute("id")); // 사용자 조회
        Homework homework = homeworkService.getHomeworkById(homeworkId); // 과제 조회

        // 파일 저장 처리
        String filePath = fileService.store(file); // 파일을 저장하고 경로를 반환
        // 파일 다운로드 URI를 생성
        String fileDownloadUri = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", filePath)
                .build().toString();

        // 새로운 UserHomework 객체를 생성하고 속성 설정
        UserHomework userHomework = new UserHomework();
        userHomework.setUser(user); // 사용자 정보 설정
        userHomework.setHomework(homework); // 과제 정보 설정
        userHomework.setDateTime(LocalDateTime.now()); // 현재 시간 설정

        // 새로운 File 객체를 생성하고 업로드된 파일 정보를 설정
        File submittedFile = new File();
        submittedFile.setFileName(file.getOriginalFilename()); // 원본 파일명 설정
        submittedFile.setFilePath(filePath); // 파일 경로 설정
        submittedFile.setUserHomework(userHomework); // 연관된 UserHomework 설정

        // UserHomework에 파일 리스트 설정
        userHomework.setFiles(List.of(submittedFile));
        // UserHomework 저장 (파일 정보와 함께)
        userHomeworkRepository.save(userHomework);

        // 응답을 위한 Map 생성
        Map<String, String> response = new HashMap<>();
        response.put("fileName", file.getOriginalFilename()); // 파일 이름
        response.put("fileDownloadUri", fileDownloadUri); // 파일 다운로드 URI
        response.put("filePath", filePath); // 저장된 파일 경로

        return response; // 응답으로 파일 정보 반환
    }

    // 파일 다운로드 처리
    // 파일 이름을 받아 해당 파일을 Resource로 로드하고, 파일을 다운로드할 수 있는 응답 생성
    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource resource = fileService.loadFileAsResource(fileName); // 파일 리소스를 로드
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"") // 파일 다운로드 설정
                .body(resource); // 파일 리소스를 응답으로 반환
    }

    // 파일 삭제 처리
    // 파일 ID를 받아 해당 파일을 삭제하고, 성공 여부를 응답
    @DeleteMapping("/deleteFile/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            fileService.deleteFileById(id); // 파일 삭제 처리
            return ResponseEntity.ok().build(); // 성공 시 200 OK 응답
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 실패 시 500 에러 응답
        }
    }
}
