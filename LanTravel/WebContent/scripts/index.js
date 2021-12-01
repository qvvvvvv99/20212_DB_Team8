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
	if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
		// alert("you're at the bottom of the page");
		postListViewFunction();
	}
};

const request = new XMLHttpRequest();
function postListViewFunction() {
	request.open("Post", "./PostLisViewServlet?scroll=" + encodeURIComponent(document.querySelector("#scroll").innerText), true);
	document.querySelector("#scroll").innerText = parseInt(document.querySelector("#scroll").innerText) + 1;
	request.onreadystatechange = postListViewProcess;
	request.send(null);
}
function postListViewProcess() {
	const postList = document.querySelector("#ajax");
	if (request.readyState == 4 && request.status == 200) {
		const object = eval('(' + request.responseText + ')');
		const result = object.result;
		console.log(result);
		for (let i = 0; i < result.length; i++) {
			const postNum = result[i][0].value;
			const viewCnt = result[i][1].value;
			const favoriteCnt = result[i][2].value;
			
			li = document.createElement("li");
			li.classList.add("box");
			
			a = document.createElement("a");
			a.href = "post.jsp?postNum=" + postNum;
			thumbnail = document.createElement("img");
			thumbnail.src = "test"; // TODO: ADD
			a.appendChild(thumbnail);
			li.appendChild(a);
			
			details = document.createElement("div");
			details.classList.add("details");
			title = document.createElement("span");
			title.classList.add("title");
			title.innerText = "test"; // TODO: ADD
			details.appendChild(title);
			
			
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
			viewCnt = document.createElement("div");
			viewCnt.classList.add("view-cnt");
			viewIcon = document.createElement("i");
			viewIcon.classList.add("fas");
			viewIcon.classList.add("fa-eye");
			viewCntSpan = document.createElement("span");
			viewCntSpan.innerText = viewCnt;
			viewCnt.appendChild(favIcon);
			viewCnt.appendChild(favCntSpan);
			cnt.appendChild(favCnt);
			cnt.appendChild(viewCnt);
			details.appendChild(cnt);
			
			li.appendChild(details);
			postList.appendChild(li);
		}
	}
}