function sortBy(criterion) {
    // 현재 활성화된 페이지 번호를 가져옴 ('.pagination .active' 클래스가 적용된 요소의 텍스트를 이용)
    const currentPage = document.querySelector('.pagination .active').textContent.trim();
    // 현재 페이지 번호와 정렬 기준(criterion)을 사용해 새 URL로 이동
    location.href = `/list/${currentPage}?sortBy=${criterion}`;
}