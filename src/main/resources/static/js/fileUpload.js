function uploadFile(homeworkId) {
    var fileInput = document.getElementById('fileInput');
    var file = fileInput.files[0];

    var formData = new FormData();
    formData.append('file', file);
    console.log(homeworkId);
    fetch((`/upload/`+ homeworkId), {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            var fileContainer = document.getElementById('fileContainer');
            var fileBox = document.createElement('div');
            fileBox.className = 'file_box';
            var fileName = document.createElement('a');
            fileName.href = data.fileDownloadUri;
            fileName.className = 'file_name';
            fileName.innerText = data.fileName;
            fileBox.appendChild(fileName);
            fileContainer.appendChild(fileBox);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function deleteFile(fileId) {
    fetch((`/deleteFile/`+fileId), {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                var fileBox = document.getElementById(`file-${fileId}`);
                if (fileBox) {
                    fileBox.remove();
                }
            } else {
                console.error('Failed to delete file');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}