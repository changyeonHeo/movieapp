function showMovieDetail(id) {
    fetch(`/movie?id=${id}`)
        .then(res => res.text())
        .then(html => {
            document.getElementById("movieDetailContent").innerHTML = html;
            document.getElementById("movieModal").style.display = "flex";
        });
}

function showActorDetail(id) {
    fetch(`/actor?id=${id}`)
        .then(res => res.text())
        .then(html => {
            document.getElementById("movieDetailContent").innerHTML = html;
            document.getElementById("movieModal").style.display = "flex";
        });
}

function closeModal() {
    document.getElementById("movieModal").style.display = "none";
    document.getElementById("movieDetailContent").innerHTML = "";
}
