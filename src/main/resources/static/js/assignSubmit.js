document.addEventListener("DOMContentLoaded", function() {
    const path = window.location.pathname;
    const isEditMode = path.includes('/homework/edit/');

    if (isEditMode) {
        const assignmentId = path.split('/').pop();
        loadAssignmentData(assignmentId);
        document.getElementById('submitBtn').textContent = '과제 수정하기';
        document.getElementById('pageTitle').textContent = '과제 수정하기';
    }
});

function submitAssignment() {
    const cate = document.getElementById('assignment_cate').value;
    const title = document.getElementById('assignment_title').value;
    const description = document.getElementById('assignment_description').value;
    const dueDate = document.getElementById('assignment_due_date').value;
    const dueTime = document.getElementById('assignment_due_time').value;

    if (!title || !description || !dueDate || !dueTime) {
        alert('모든 항목을 입력해주세요.');
        return;
    }

    const assignmentData = {
        cate: cate,
        title: title,
        description: description,
        dueDate: dueDate,
        dueTime: dueTime
    };

    const path = window.location.pathname;
    const isEditMode = path.includes('/homework/edit/');
    const method = isEditMode ? 'PUT' : 'POST';
    const url = isEditMode ? `/homework/post/${path.split('/').pop()}` : '/homework/post';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(assignmentData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('과제 부여에 실패했습니다.');
        })
        .then(data => {
            alert(isEditMode ? '과제가 성공적으로 수정되었습니다.' : '과제가 성공적으로 부여되었습니다.');
            window.location.href = '/list/1';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('과제 부여 중 오류가 발생했습니다.');
        });
}
