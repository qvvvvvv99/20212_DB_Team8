// picture slide
const slide = document.querySelector(".slide");
const pictures = slide.querySelector(".pictures");
const picture = pictures.querySelectorAll("li");
const prevBtn = document.querySelector(".prev");
const nextBtn = document.querySelector(".next");

const picCnt = picture.length;
const picWidth = slide.offsetWidth;
const picMargin = parseInt(getComputedStyle(picture[0]).marginRight);

var currIdx = 0;

function moveSlide(num) {
	pictures.style.left = -num * picWidth + "px";
	currIdx = num;
}

pictures.style.width = picWidth * picCnt - picMargin + "px";

prevBtn.addEventListener("click", () => {
	if (currIdx > 0) {
		moveSlide(currIdx - 1);
	} else {
		moveSlide(picCnt - 1);
	}
});

nextBtn.addEventListener("click", () => {
	if (currIdx < picCnt - 1) {
		moveSlide(currIdx + 1);
	} else {
		moveSlide(0);
	}
});

// write re-reply
const replyBtns = document.querySelectorAll(".reply-btn");
const myReply = document.querySelector(".my-reply");

const createReplyWriter = (item) => {
	reReply = myReply.cloneNode(true);
	reReply.style.display = "none";
	reReply.querySelector("textarea").value = "";
	if (item.parentNode.querySelector(".my-reply") == null) {
		item.parentNode.appendChild(reReply);
	}
};

const displayReplyWriter = (item) => {
	item.parentNode.querySelector(".my-reply").style.display = "block";
};

replyBtns.forEach((replyBtn) => {
	createReplyWriter(replyBtn);
	replyBtn.addEventListener("click", () => displayReplyWriter(replyBtn));
});

// reply input
const textareas = document.querySelectorAll("textarea");
const initHeight = textareas[0].scrollHeight + 2;

textareas.forEach((textarea) => {
	// auto-resize reply input
	textarea.addEventListener("keyup", (e) => {
		textarea.style.height = initHeight + "px";
		let scHeight = e.target.scrollHeight + 2;
		textarea.style.height = scHeight + "px";
	});
	// display reply-buttons
	textarea.addEventListener("click", () => {
		textarea.parentNode.querySelector(".reply-buttons").style.display = "flex";
	});
});

// cancel reply
const cancelBtns = document.querySelectorAll(".reply-buttons > button.cancel");
cancelBtns.forEach((cancelBtn) =>
	cancelBtn.addEventListener("click", () => {
		const replyBtns = cancelBtn.parentNode;
		replyBtns.parentNode.querySelector("textarea").value = "";
		replyBtns.style.display = "none";
		if (replyBtns.parentNode.parentNode.nodeName != "SECTION") {
			// 대댓글(답글) O, 댓글 X
			replyBtns.parentNode.style.display = "none";
		}
	})
);

// write reply
const writeBtns = document.querySelectorAll(".reply-buttons > button.write");
writeBtns.forEach((writeBtn) =>
	writeBtn.addEventListener("click", () => {
		const replyBtns = writeBtn.parentNode;
		if (replyBtns.parentNode.parentNode.nodeName != "SECTION") {
			pNum = -1;
		} else {
			pNum = writeBtns[1].parentNode.parentNode.parentNode.parentNode.querySelector(".replyNum").innerText;
		}
		text = writeBtn.parentNode.parentNode.querySelector("textarea").value;
		tNum = 101;	// TODO: ADD
		postNum = document.querySelector("#postNum").innerText;
		if (text != "") {
			replyWriteFunction(text, pNum, tNum, postNum);
			replyViewFunction();
		}
		replyBtns.parentNode.querySelector("textarea").value = "";
		replyBtns.style.display = "none";
		if (replyBtns.parentNode.parentNode.nodeName != "SECTION") {
			// 대댓글(답글) O, 댓글 X
			replyBtns.parentNode.style.display = "none";
		}
	})
);

// AJAX
const request = new XMLHttpRequest();
function replyWriteFunction(text, pNum, tNum, postNum) {
	request.open("Post", "./ReplyWriteServlet?postNum=" + encodeURIComponent(document.querySelector("#postNum").innerText) + "&text=" + encodeURIComponent(text)  + "&pNum=" + encodeURIComponent(pNum) + "&tNum=" + encodeURIComponent(tNum), true);
	request.send(null);
}


function replyViewFunction() {
	request.open("Post", "./ReplyViewServlet?postNum=" + encodeURIComponent(document.querySelector("#postNum").innerText), true);
	request.onreadystatechange = replyViewProcess;
	request.send(null);
}
function replyViewProcess() {
	const replies = document.querySelector("#ajax");
	replies.innerHTML = "";
	if (request.readyState == 4 && request.status == 200) {
		const object = eval('(' + request.responseText + ')');
		const result = object.result;
		for (let i = 0; i < result.length; i++) {
			const text = result[i][2].value;
			const writer = result[i][3].value;
			const writtenTime = result[i][4].value;
			const depth = result[i][5].value;

			reply = document.createElement("div");
			reply.classList.add("reply");
			reply.style.marginLeft = 30 * depth + "px";

			replyNum = document.createElement("div");
			replyNum.classList.add("replyNum");
			replyNum.classList.add("hidden");
			reply.appendChild(replyNum);

			if (depth > 0) {
				indentation = document.createElement("div");
				indentation.classList.add("indentation");
				indentation.innerText = "└";
				reply.appendChild(indentation);
			}

			replyHead = document.createElement("div");
			replyHead.classList.add("reply-head");
			replyWriter = document.createElement("div");
			replyWriter.classList.add("reply-writer")
			replyWriter.innerText = writer;
			replyHead.appendChild(replyWriter);
			reply.appendChild(replyHead);

			replyBody = document.createElement("div");
			replyBody.classList.add("reply-body");
			p = document.createElement("p");
			p.innerText = text;
			replyBody.appendChild(p);
			reply.appendChild(replyBody);

			replyTail = document.createElement("div");
			replyTail.classList.add("reply-tail");
			replyTime = document.createElement("div");
			replyTime.classList.add("reply-time");
			replyTime.innerText = writtenTime;
			replyBtn = document.createElement("button");
			replyBtn.classList.add("reply-btn");
			replyBtn.innerText = "답글";
			reportBtn = document.createElement("button");
			reportBtn.classList.add("report-btn");
			reportBtn.classList.add("open-report-modal");
			reportBtn.innerText = "신고";
			replyTail.appendChild(replyTime);
			replyTail.appendChild(replyBtn);
			replyTail.appendChild(reportBtn);
			reply.appendChild(replyTail);

			replies.appendChild(reply);
		}
	}
}


// modal windows(share, report)
const openShareModalBtns = document.querySelectorAll(".open-share-modal");
const openReportModalBtns = document.querySelectorAll(".open-report-modal");
const modals = document.querySelector(".modals");
const shareModal = modals.querySelector(".share.modal");
const reportModal = modals.querySelector(".report.modal");
const shareModalOverlay = shareModal.querySelector(".share .modal-overlay");
const reportModalOverlay = reportModal.querySelector(".report .modal-overlay");
const closeShareModalBtns = document.querySelectorAll(".share .close-modal");
const closeReportModalBtns = document.querySelectorAll(".report .close-modal");

const openModal = (modal) => {
	modals.classList.remove("hidden");
	modal.classList.remove("hidden");
};
const closeModal = (modal) => {
	modals.classList.add("hidden");
	modal.classList.add("hidden");
};

openShareModalBtns.forEach((btn) => btn.addEventListener("click", () => openModal(shareModal)));
openReportModalBtns.forEach((btn) => btn.addEventListener("click", () => openModal(reportModal)));
shareModalOverlay.addEventListener("click", () => closeModal(shareModal));
reportModalOverlay.addEventListener("click", () => closeModal(reportModal));
closeShareModalBtns.forEach((btn) => btn.addEventListener("click", () => closeModal(shareModal)));
closeReportModalBtns.forEach((btn) => btn.addEventListener("click", () => closeModal(reportModal)));
