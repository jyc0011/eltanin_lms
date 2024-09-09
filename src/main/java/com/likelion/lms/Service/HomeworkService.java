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
    public HomeworkService(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    // 전체 조회
    public List<Homework> getAllHomework()  {
        return homeworkRepository.findAll();
    }

    //페이징 조회
    public Page<Homework> getHomeworkByPageAndSort(int page, int size, String criterion) {
        Sort sort = Sort.by(Sort.Direction.ASC, criterion.equals("createdDate") ? "createdDate" : "dueDate");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return homeworkRepository.findAll(pageRequest);
    }

    // 세부 조회
    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다. ID: " + id));
    }
    // 관리자 세부조회
    public List<UserHomework> getSubmittedFilesByHomeworkId(Long homeworkId) {
        return userHomeworkRepository.findByHomeworkId(homeworkId);
    }

    // 저장
    public Homework saveHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }

    // 수정
    public Homework updateHomework(Long id, Homework updatedHomework) {
        return homeworkRepository.findById(id).map(homework -> {
            homework.setTitle(updatedHomework.getTitle());
            homework.setDescription(updatedHomework.getDescription());
            homework.setDueDate(updatedHomework.getDueDate());
            homework.setDueTime(updatedHomework.getDueTime());
            return homeworkRepository.save(homework);
        }).orElseThrow(() -> new IllegalArgumentException("해당 과제가 존재하지 않습니다. ID: " + id));
    }

    // 삭제
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id);
    }

    public List<UserHomework> getSubmittedFilesByHomeworkIdAndUserId(Long homeworkId, Long userId) {
        return userHomeworkRepository.findByHomeworkIdAndUserId(homeworkId, userId);
    }

    public Map<User, List<UserHomework>> getSubmittedFilesGroupedByUser(Long homeworkId) {
        List<UserHomework> submittedFiles = userHomeworkRepository.findByHomeworkId(homeworkId);

        // 제출한 사용자별로 파일을 그룹화
        return submittedFiles.stream()
                .collect(Collectors.groupingBy(UserHomework::getUser));
    }

}