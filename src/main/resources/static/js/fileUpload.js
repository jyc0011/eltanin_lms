function uploadFile(homeworkId) {
    // 파일 입력 요소에서 사용자가 선택한 파일을 가져옴
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0]; // 파일 입력 필드에서 첫 번째 파일을 가져옴
    // 파일을 서버로 전송하기 위해 FormData 객체 생성
    var formData = new FormData();
    formData.append('file', file); // FormData에 'file'이라는 이름으로 파일을 추가
    console.log(homeworkId); // 콘솔에 과제 ID 출력 (디버깅용)
    // 파일을 업로드할 경로로 POST 요청을 보냄
    fetch((`/upload/` + homeworkId), {
        method: 'POST', // POST 메서드를 사용해 파일 전송
        body: formData // FormData를 요청 본문으로 전달
    })
        .then(response => response.json()) // 서버 응답을 JSON으로 처리
        .then(data => {
            // 파일 업로드 성공 시 UI에 파일 정보를 추가
            var fileContainer = document.getElementById('fileContainer');
            var fileBox = document.createElement('div');
            fileBox.className = 'file_box'; // 파일을 보여줄 div 요소 생성
            var fileName = document.createElement('a');
            fileName.href = data.fileDownloadUri; // 파일 다운로드 URI를 설정
            fileName.className = 'file_name';
            fileName.innerText = data.fileName; // 파일 이름을 링크로 설정
            fileBox.appendChild(fileName); // 파일 링크를 파일 박스에 추가
            fileContainer.appendChild(fileBox); // 파일 박스를 컨테이너에 추가
        })
        .catch(error => {
            // 에러 발생 시 콘솔에 에러 출력
            console.error('Error:', error);
        });
}

function deleteFile(fileId) {
    // 서버로 DELETE 요청을 보내어 파일 삭제
    fetch((`/deleteFile/` + fileId), {
        method: 'DELETE' // DELETE 메서드를 사용해 파일 삭제 요청
    })
        .then(response => {
            if (response.ok) { // 서버에서 OK 응답이 오면
                // 해당 파일 박스(fileId)를 찾아서 DOM에서 제거
                var fileBox = document.getElementById(`file-${fileId}`);
                if (fileBox) {
                    fileBox.remove(); // 파일 박스 삭제
                }
            } else {
                console.error('Failed to delete file'); // 응답이 실패하면 에러 출력
            }
        })
        .catch(error => {
            // 에러 발생 시 콘솔에 에러 출력
            console.error('Error:', error);
        });
}