function sortBy(criterion) {
    const currentPage = document.querySelector('.pagination .active').textContent.trim();
    location.href = `/list/${currentPage}?sortBy=${criterion}`;
}