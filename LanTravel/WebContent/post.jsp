<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="post.Post"%>
<%@ page import="post.PostDAO"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Lan Travel</title>
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
<link rel="stylesheet"
	href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
<link rel="stylesheet" href="styles/post.css" />
</head>
<body>
	<header>
		<!-- logo -->
		<div class="logo-area">
			<h1 class="logo">
				<a href="index.jsp"> <span>LanTravel</span> <!-- logo image 추가 후 span에 class="hidden" 추가-->
				</a>
			</h1>
		</div>
		<!-- menu -->
		<nav>
			<ul class="menu">
				<li class="menu-item hidden"><a href="login.jsp"><i
						class="fas fa-sign-in-alt"></i></a></li>
				<li class="menu-item"><a href="#"><i class="fas fa-heart"></i></a>
				</li>
				<li class="menu-item"><a href="#"><i class="fas fa-user"></i></a>
				</li>
				<li class="menu-item"><a href="write.jsp"><i
						class="fas fa-pen-nib"></i></a></li>
			</ul>
		</nav>
	</header>
	<main>
		<%
		System.out.println(request.getParameter("postNum"));
		if (request.getParameter("postNum") == null) {
			out.println("<script>location.href = 'index.jsp';</script>");
			return;
		}
		int postNum = Integer.parseInt(request.getParameter("postNum"));
		if (postNum == 0) {
			out.println("<script>location.href = 'index.jsp';</script>");
			return;
		}
		System.out.println(postNum);
		PostDAO postDao = new PostDAO();
		Post post = postDao.getPost(postNum);
		postDao.increaseViewCnt(postNum);
		post.setViewCnt(post.getViewCnt() + 1); // 조회 수 증가
		%>
		<article class="post">
			<section class="slide">
				<ul class="pictures">
					<li>1<img src="" alt="" /></li>
					<li>2<img src="" alt="" /></li>
					<li>3<img src="" alt="" /></li>
					<li>4<img src="" alt="" /></li>
					<li>5<img src="" alt="" /></li>
				</ul>
				<button class="prev">
					<i class="fas fa-angle-left"></i>
				</button>
				<button class="next">
					<i class="fas fa-angle-right"></i>
				</button>
			</section>
			<section class="writing">
				<p><%=post.getText()%></p>
			</section>
			<section class="replies">
				<h3>댓글 5개</h3>
				<div class="my-reply">
					<textarea placeholder="댓글 작성" name="reply-text" id="reply-text"
						maxlength="4000"></textarea>
					<div class="reply-buttons">
						<button class="cancel">취소</button>
						<button class="write">작성</button>
					</div>
				</div>
				<div class="reply">
					<div class="reply-head">
						<div class="reply-writer">작성자</div>
					</div>
					<div class="reply-body">
						<p>Lorem ipsum dolor sit amet consectetur adipisicing elit.
							Corporis, nihil illo provident exercitationem assumenda, eveniet
							minima debitis est blanditiis labore nisi sit dolores repellat.
							Recusandae in officiis placeat! Voluptatibus, deleniti.</p>
					</div>
					<div class="reply-tail">
						<div class="reply-time">2021.02.15. Sun. 09:38:27</div>
						<button class="reply-btn">답글</button>
						<button class="report-btn open-report-modal">신고</button>
					</div>
				</div>
			</section>
		</article>
		<article class="detail">
			<section class="travel-info">
				<h2 class="title">안면도</h2>
				<div class="location">대한민국 태안군</div>
				<div class="period"><%=post.getStartDate()%>
					~
					<%=post.getEndDate()%></div>
			</section>
			<section class="counts-action">
				<div>
					<section class="counts">
						<div>
							<div class="favorite">
								<i class="fas fa-heart"></i>
								<div class="cnt"><%=post.getBookmarkCnt()%></div>
							</div>
							<div class="view">
								<i class="far fa-eye"></i>
								<div class="cnt"><%=post.getViewCnt()%></div>
							</div>
						</div>
					</section>
					<section class="actions">
						<div>
							<div class="share-btn open-share-modal">
								<i class="fas fa-share"></i>
							</div>
							<div class="report-btn open-report-modal">
								<i class="fas fa-flag"></i>
								</d>
							</div>
					</section>
				</div>
			</section>
			<section class="post-info">
				<div class="tags">
					<div class="tag">Tag1</div>
					<div class="tag">Tag2</div>
					<div class="tag">Tag3</div>
				</div>
				<div class="written">
					<a class="writer">Nickname</a>
					<div class="written-time"><%=post.getWrittenTime()%></div>
				</div>
			</section>
		</article>
		<article class="modals hidden">
			<section class="share modal hidden">
				<div class="modal-overlay"></div>
				<div class="modal-content">
					<div class="share-ex"></div>
					<div class="share-buttons">
						<button class="cancel close-modal">취소</button>
					</div>
				</div>
			</section>
			<section class="report modal hidden">
				<div class="modal-overlay"></div>
				<div class="modal-content">
					<textarea placeholder="신고 작성" name="report-text" id="report-text"
						maxlength="4000"></textarea>
					<div class="report-buttons">
						<button class="cancel close-modal">취소</button>
						<button class="write close-modal">작성</button>
					</div>
				</div>
			</section>
		</article>
		<div id="padding"></div>
	</main>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
	<script src="scripts/post.js"></script>
</body>
</html>