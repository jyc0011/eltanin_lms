package com.likelion.lms.Service;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.User;
import com.likelion.lms.Domain.UserHomework;
import com.likelion.lms.Repository.HomeworkRepository;
import com.likelion.lms.Repository.UserHomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeworkService {

    @Autowired
    private final HomeworkRepository homeworkRepository;
    @Autowired
    private UserHomeworkRepository userHomeworkRepository;

    // 생성자 주입: HomeworkRepository를 주입 받음
    public HomeworkService(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    // 모든 과제를 조회
    public List<Homework> getAllHomework()  {
        return homeworkRepository.findAll(); // 모든 과제 목록을 반환
    }

    // 페이징과 정렬을 통해 과제를 조회
    // 주어진 페이지와 정렬 기준에 따라 과제를 페이징 처리하고 반환
    public Page<Homework> getHomeworkByPageAndSort(int page, int size, String criterion) {
        // 정렬 기준이 'createdDate'이면 생성일 기준으로, 그렇지 않으면 마감일 기준으로 정렬
        Sort sort = Sort.by(Sort.Direction.ASC, criterion.equals("createdDate") ? "createdDate" : "dueDate");
        PageRequest pageRequest = PageRequest.of(page, size, sort); // 페이지 요청 생성
        return homeworkRepository.findAll(pageRequest); // 페이징 처리된 과제 목록 반환
    }

    // 과제 세부 정보 조회
    // 과제 ID를 통해 해당 과제를 조회하고, 없을 경우 예외 발생
    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다. ID: " + id));
    }

    // 관리자용 제출 파일 조회
    // 과제 ID를 통해 해당 과제에 제출된 모든 파일 목록을 조회
    public List<UserHomework> getSubmittedFilesByHomeworkId(Long homeworkId) {
        return userHomeworkRepository.findByHomeworkId(homeworkId); // 과제 ID로 제출된 파일 조회
    }

    // 새로운 과제 저장
    // 새로 작성된 과제를 저장하고 반환
    public Homework saveHomework(Homework homework) {
        return homeworkRepository.save(homework); // 과제 저장
    }

    // 과제 수정
    // 수정할 과제 ID를 받아 과제 내용을 업데이트 후 저장
    public Homework updateHomework(Long id, Homework updatedHomework) {
        return homeworkRepository.findById(id).map(homework -> {
            // 기존 과제의 제목, 설명, 마감일을 업데이트
            homework.setTitle(updatedHomework.getTitle());
            homework.setDescription(updatedHomework.getDescription());
            homework.setDueDate(updatedHomework.getDueDate());
            homework.setDueTime(updatedHomework.getDueTime());
            return homeworkRepository.save(homework); // 업데이트 후 저장
        }).orElseThrow(() -> new IllegalArgumentException("해당 과제가 존재하지 않습니다. ID: " + id)); // 과제가 없을 경우 예외 처리
    }

    // 과제 삭제
    // 과제 ID로 해당 과제를 삭제
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id); // 과제 삭제
    }

    // 사용자가 제출한 파일 조회
    // 특정 과제와 사용자 ID를 기반으로 제출한 파일 목록을 조회
    public List<UserHomework> getSubmittedFilesByHomeworkIdAndUserId(Long homeworkId, Long userId) {
        return userHomeworkRepository.findByHomeworkIdAndUserId(homeworkId, userId); // 과제 ID와 사용자 ID로 제출 파일 조회
    }

    // 제출한 사용자별로 파일을 그룹화하여 조회
    // 관리자 화면에서 각 사용자가 제출한 파일을 그룹화하여 보여줄 때 사용
    public Map<User, List<UserHomework>> getSubmittedFilesGroupedByUser(Long homeworkId) {
        List<UserHomework> submittedFiles = userHomeworkRepository.findByHomeworkId(homeworkId); // 과제 ID로 제출된 파일 조회

        // 사용자별로 제출한 파일 목록을 그룹화하여 반환
        return submittedFiles.stream()
                .collect(Collectors.groupingBy(UserHomework::getUser));
    }
}