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
		alert("you're at the bottom of the page");
	}
};