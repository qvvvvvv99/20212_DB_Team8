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
    console.log(writeBtn.parentNode.parentNode.querySelector("textarea").value); // TODO: 수정
    replyBtns.parentNode.querySelector("textarea").value = "";
    replyBtns.style.display = "none";
    if (replyBtns.parentNode.parentNode.nodeName != "SECTION") {
      // 대댓글(답글) O, 댓글 X
      replyBtns.parentNode.style.display = "none";
    }
  })
);

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
