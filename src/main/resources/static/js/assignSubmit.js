document.addEventListener("DOMContentLoaded", function() {
    // 현재 경로를 가져옴
    const path = window.location.pathname;
    // URL에 '/homework/edit/'가 포함되어 있으면 수정 모드로 판단
    const isEditMode = path.includes('/homework/edit/');

    if (isEditMode) {
        // URL의 마지막 부분에서 과제 ID를 추출
        const assignmentId = path.split('/').pop();
        // 과제 데이터를 불러오는 함수 호출 (수정 모드일 때)
        loadAssignmentData(assignmentId);
        // 버튼과 제목을 '과제 수정하기'로 변경
        document.getElementById('submitBtn').textContent = '과제 수정하기';
        document.getElementById('pageTitle').textContent = '과제 수정하기';
    }
});

function submitAssignment() {
    // 입력 필드에서 과제 데이터 수집
    const cate = document.getElementById('assignment_cate').value;
    const title = document.getElementById('assignment_title').value;
    const description = document.getElementById('assignment_description').value;
    const dueDate = document.getElementById('assignment_due_date').value;
    const dueTime = document.getElementById('assignment_due_time').value;
    // 필수 입력 항목이 비어 있을 경우 경고 메시지를 띄움
    if (!title || !description || !dueDate || !dueTime) {
        alert('모든 항목을 입력해주세요.');
        return;
    }
    // 과제 데이터를 객체로 생성
    const assignmentData = {
        cate: cate,
        title: title,
        description: description,
        dueDate: dueDate,
        dueTime: dueTime
    };
    // 현재 경로에서 수정 모드인지 여부를 확인
    const path = window.location.pathname;
    const isEditMode = path.includes('/homework/edit/');
    // 수정 모드일 경우 PUT, 새로 작성할 경우 POST 메서드 설정
    const method = isEditMode ? 'PUT' : 'POST';
    // 수정 모드일 경우 해당 과제 ID로 URL 설정, 아니면 새 과제 작성 URL 설정
    const url = isEditMode ? `/homework/post/${path.split('/').pop()}` : '/homework/post';
    // 서버로 요청 보내기
    fetch(url, {
        method: method, // PUT 또는 POST 메서드
        headers: {
            'Content-Type': 'application/json' // JSON 형식의 데이터 전송
        },
        body: JSON.stringify(assignmentData) // 수집한 과제 데이터를 JSON으로 변환하여 본문에 추가
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // 응답이 성공적이면 JSON 데이터로 처리
            }
            throw new Error('과제 부여에 실패했습니다.'); // 실패 시 에러 던짐
        })
        .then(data => {
            // 성공 시 메시지를 띄우고 목록 페이지로 이동
            alert(isEditMode ? '과제가 성공적으로 수정되었습니다.' : '과제가 성공적으로 부여되었습니다.');
            window.location.href = '/list/1'; // 첫 번째 페이지로 리디렉션
        })
        .catch(error => {
            // 에러 발생 시 콘솔에 에러 출력 및 알림
            console.error('Error:', error);
            alert('과제 부여 중 오류가 발생했습니다.');
        });
}