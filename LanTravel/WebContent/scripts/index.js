// dropdown
const header = document.querySelector("header");
const searchForm = document.querySelector(".search-form");
const searchBox = searchForm.querySelector(".search-box");
const dropdown = searchForm.querySelector(".dropdown");
const ddButton = dropdown.querySelector(".dropdown-button");
const searchType = ddButton.querySelector(".search-type");
const ddOptions = dropdown.querySelectorAll(".option-item");

const handleSelect = (item) => {
	dropdown.classList.remove("active");
	searchType.innerHTML = item.textContent;
	if (searchBox.value.length > 0) {
		searchForm.submit();
	}
};

ddButton.addEventListener("click", () => {
	if (dropdown.classList.contains("active")) {
		dropdown.classList.remove("active");
	} else {
		dropdown.classList.add("active");
	}
});

ddOptions.forEach((option) => {
	option.addEventListener("click", () => {
		option.previousElementSibling.checked = true;
		handleSelect(option);
	});
});

// scroll
window.onscroll = () => {
	if (window.scrollY > 268) {
		if (!header.classList.contains("scrolled")) {
			header.classList.add("scrolled");
		}
		if (!searchForm.classList.contains("scrolled")) {
			searchForm.classList.add("scrolled");
		}
	} else {
		if (header.classList.contains("scrolled")) {
			header.classList.remove("scrolled");
		}
		if (searchForm.classList.contains("scrolled")) {
			searchForm.classList.remove("scrolled");
		}
	}

	// infinite scroll
	if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 1) {
		postListViewFunction();
	}
};

const request = new XMLHttpRequest();
function postListViewFunction() {
	request.open("Post", "./PostListViewServlet?scroll=" + encodeURIComponent(document.querySelector("#scroll").innerText), true);
	document.querySelector("#scroll").innerText = parseInt(document.querySelector("#scroll").innerText) + 1;
	request.onreadystatechange = postListViewProcess;
	request.send(null);
}
function postListViewProcess() {
	const postList = document.querySelector("#ajax");
	if (request.readyState == 4 && request.status == 200) {
		const object = eval('(' + request.responseText + ')');
		const result = object.result;
		for (let i = 0; i < result.length; i++) {
			const postNum = result[i][0].value;
			const viewCnt = result[i][1].value;
			const favoriteCnt = result[i][2].value;
			const title = result[i][3].value;
			const thumbnailSrc = result[i][4].value;
			
			li = document.createElement("li");
			li.classList.add("box");
			
			a = document.createElement("a");
			a.href = "post.jsp?postNum=" + postNum;
			thumbnail = document.createElement("img");
			thumbnail.classList.add("thumbnail");
			thumbnail.src = thumbnailSrc;
			a.appendChild(thumbnail);
			li.appendChild(a);
			
			details = document.createElement("div");
			details.classList.add("details");
			titleSpan = document.createElement("span");
			titleSpan.classList.add("title");
			titleSpan.innerText = title;
			details.appendChild(titleSpan);
			
			cnt = document.createElement("div");
			cnt.classList.add("cnt");
			favCnt = document.createElement("div");
			favCnt.classList.add("fav-cnt");
			favIcon = document.createElement("i");
			favIcon.classList.add("fas");
			favIcon.classList.add("fa-heart");
			favCntSpan = document.createElement("span");
			favCntSpan.innerText = favoriteCnt;
			favCnt.appendChild(favIcon);
			favCnt.appendChild(favCntSpan);
			vCnt = document.createElement("div");
			vCnt.classList.add("view-cnt");
			vIcon = document.createElement("i");
			vIcon.classList.add("fas");
			vIcon.classList.add("fa-eye");
			vCntSpan = document.createElement("span");
			vCntSpan.innerText = viewCnt;
			vCnt.appendChild(vIcon);
			vCnt.appendChild(vCntSpan);
			cnt.appendChild(favCnt);
			cnt.appendChild(vCnt);
			details.appendChild(cnt);
			
			li.appendChild(details);
			postList.appendChild(li);
		}
	}
}