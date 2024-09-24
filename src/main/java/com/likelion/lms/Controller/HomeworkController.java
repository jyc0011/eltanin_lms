package com.likelion.lms.Controller;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.User;
import com.likelion.lms.Domain.UserHomework;
import com.likelion.lms.Service.HomeworkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class HomeworkController {
    // 의존성 자동 주입을 위해 이용
    @Autowired
    private HomeworkService homeworkService;

    // 목록 조회
    // {page}는 path variable이며, url에 삽입 되는 정보.
    // request param으로 전달되는 sortBy는 query string.
    // 둘의 공통점은 URL에 정보를 추가적으로 더 줄 수 있다는 것. 즉, 요청을 동적으로 처리하게 함.
    // 차이점은 path variable의 경우는 경로를 변수로 쓰고 (ex: /1 )
    // query string은 주소를 매핑하여 쓴다는 것(ex: ?sortBy=new)
    // 정렬 기준이 없는 경우 기본값으로 'createdDate'로 정렬
    // 페이지의 크기를 5로 설정하고, pageIndex를 계산하여 페이징 처리 후 목록 조회
    @GetMapping("/list/{page}")
    public String homework_list(
            @PathVariable int page,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            Model model, HttpSession session) {
        int size = 5;
        int pageIndex = page - 1; // 페이지는 0부터 시작하므로, -1 해줌
        Page<Homework> homeworkPage = homeworkService.getHomeworkByPageAndSort(pageIndex, size, sortBy);
        List<Homework> homeworkList = homeworkPage.getContent(); // 조회된 과제 목록
        model.addAttribute("homeworkList", homeworkList); // 모델에 과제 목록 추가
        model.addAttribute("currentPage", page); // 현재 페이지 번호 전달
        model.addAttribute("totalPages", homeworkPage.getTotalPages()); // 전체 페이지 수 전달
        model.addAttribute("sortBy", sortBy); // 정렬 기준 전달
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin"); // 세션에서 관리자 여부 확인
        model.addAttribute("isAdmin", isAdmin); // 관리자 여부 전달

        return "homework/homework_list"; // 과제 목록 페이지로 이동
    }

    // 과제 세부 조회
    // 과제 ID를 통해 해당 과제 정보를 조회하고 관리자와 일반 사용자에 따라 다른 화면을 보여줌
    @GetMapping("/homework/{id}")
    public String homework_detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin"); // 세션에서 관리자 여부 확인
        Homework homework = homeworkService.getHomeworkById(id); // 과제 ID를 통해 과제 조회
        if (isAdmin != null && isAdmin) {
            // 관리자인 경우 제출된 파일을 유저별로 그룹핑하여 조회
            Map<User, List<UserHomework>> submittedFilesGroupedByUser = homeworkService.getSubmittedFilesGroupedByUser(id);
            model.addAttribute("homework", homework); // 과제 정보 추가
            model.addAttribute("submittedFilesGroupedByUser", submittedFilesGroupedByUser); // 제출된 파일 목록 추가
            return "homework/homework_admin"; // 관리자용 상세 페이지로 이동
        } else {
            // 일반 사용자일 경우, 해당 과제에 대한 본인의 제출 파일 조회
            Long userId = (Long) session.getAttribute("id"); // 세션에서 사용자 ID 확인
            List<UserHomework> userSubmittedFiles = homeworkService.getSubmittedFilesByHomeworkIdAndUserId(id, userId);
            model.addAttribute("homework", homework); // 과제 정보 추가
            model.addAttribute("submittedFiles", userSubmittedFiles); // 본인이 제출한 파일 목록 추가
            return "homework/homework"; // 일반 사용자용 상세 페이지로 이동
        }
    }


    // 새 과제 작성 폼으로 이동
    // GET 요청으로 과제 작성 페이지를 반환
    @GetMapping("/homework/new")
    public String homework_new() {
        return "homework/write"; // 과제 작성 페이지로 이동
    }

    // 새로운 과제 업로드
    // @RequestBody로 전달받은 Homework 객체를 저장하고 생성된 과제를 반환
    @PostMapping("/homework/post")
    public ResponseEntity<Homework> createHomework(@RequestBody Homework homework) {
        homework.setCreatedDate(LocalDateTime.now()); // 현재 시간으로 생성일자 설정
        Homework createdHomework = homeworkService.saveHomework(homework); // 과제 저장
        return ResponseEntity.ok(createdHomework); // 저장된 과제 정보를 응답으로 반환
    }

    // 과제 수정 폼으로 이동
    // 수정할 과제의 ID를 받아 해당 과제 정보를 조회하고 수정 페이지로 이동
    @GetMapping("/homework/edit/{id}")
    public String homework_edit(@PathVariable Long id, Model model) {
        Homework homework = homeworkService.getHomeworkById(id); // 과제 ID로 과제 조회
        if (homework == null) {
            throw new IllegalArgumentException("해당 ID의 과제가 존재하지 않습니다: " + id); // 과제가 없을 시 예외 발생
        }
        model.addAttribute("homework", homework); // 과제 정보 추가
        return "homework/write"; // 수정 페이지로 이동
    }

    // 과제 수정 저장
    // @PathVariable로 수정할 과제의 ID를 받으며, @RequestBody로 과제 데이터를 받아 업데이트 수행
    @PutMapping("/homework/post/{id}")
    public ResponseEntity<Homework> updateHomework(@PathVariable Long id, @RequestBody Homework homework) {
        Homework updatedHomework = homeworkService.updateHomework(id, homework); // 과제 업데이트
        return ResponseEntity.ok(updatedHomework); // 업데이트된 과제 정보를 응답으로 반환
    }
}
