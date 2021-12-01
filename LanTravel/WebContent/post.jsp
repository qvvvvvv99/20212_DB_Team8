<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Stack"%>
<%@ page import="user.User"%>
<%@ page import="user.UserDAO"%>
<%@ page import="post.Post"%>
<%@ page import="post.PostDAO"%>
<%@ page import="post.Reply"%>
<%@ page import="post.ReplyDAO"%>
<%@ page import="post.Location"%>
<%@ page import="post.LocationDAO"%>
<%@ page import="post.Picture"%>
<%@ page import="post.PictureDAO"%>
<%@ page import="post.Tag"%>
<%@ page import="post.TagDAO"%>
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
	<%
	request.setCharacterEncoding("UTF-8");

	int userType = 1; // 1: Guest, 2: Traveler, 3: Admin

	if (session.getAttribute("userType") != null) {
		userType = (int) session.getAttribute("userType");
	}
	%>
	<header>
		<!-- logo -->
		<div class="logo-area">
			<h1 class="logo">
				<%
				if (userType == 3) {
				%>
				<a href="adminMain.jsp"><img src="images/logo/logo.png"> <span class="hidden">LanTravel</span>
				</a>
				<%
				} else {
				%>
				<a href="index.jsp"><img src="images/logo/logo.png"> <span class="hidden">LanTravel</span>
				</a>
				<%
				}
				%>
			</h1>
		</div>
		<!-- menu -->
		<nav>
			<ul class="menu">
			<% if(userType==1) {%>
				<li class="menu-item"><a href="login.jsp"><i
						class="fas fa-sign-in-alt"></i></a></li>
			<%} %>
			<% if(userType==2) {%>
				<li class="menu-item"><a href="#"><i class="fas fa-heart"></i></a>
				</li>
				<li class="menu-item"><a href="user.jsp"><i class="fas fa-user"></i></a>
				</li>
				<li class="menu-item"><a href="write.jsp"><i
						class="fas fa-pen-nib"></i></a></li>
			<%} %>
			<% if(userType==3) {%>
				<li class="menu-item"><a href="user.jsp"><i class="fas fa-user"></i></a>
				</li>
			<%} %>
			</ul>
		</nav>
	</header>
	<main>
		<%
		if (request.getParameter("postNum") == null) {
			out.println("<script>location.href = 'index.jsp';</script>");
			return;
		}
		int postNum = Integer.parseInt(request.getParameter("postNum"));
		if (postNum == 0) {
			out.println("<script>location.href = 'index.jsp';</script>");
			return;
		}
		session.setAttribute("postNum", postNum);
		PostDAO postDao = new PostDAO();
		Post post = postDao.getPost(postNum);
		postDao.increaseViewCnt(postNum);
		post.setViewCnt(post.getViewCnt() + 1); // 조회 수 증가
		%>
		<article class="post">
			<div id="postNum" class="hidden"><%=postNum%></div>
			<section class="slide">
				<%
				ArrayList<Picture> pictures = new PictureDAO().getPictures(postNum);
				%>
				<ul class="pictures">
					<%
					for (Picture picture : pictures) {
					%>
					<li><img src=<%=picture.getLink()%> alt="" /></li>
					<%
					}
					%>
				</ul>
				<%
				if (pictures.size() > 1) {
				%>
				<button class="prev">
					<i class="fas fa-angle-left"></i>
				</button>
				<button class="next">
					<i class="fas fa-angle-right"></i>
				</button>
				<%
				}
				%>
			</section>
			<section class="writing">
				<p><%=post.getText()%></p>
			</section>
			<section class="replies">
				<%
				ArrayList<Reply> replies = new ReplyDAO().getReplies(postNum);
				%>
				<h3>
					댓글
					<%=replies.size()%>개
				</h3>
				<div class="my-reply">
					<textarea placeholder="댓글 작성" name="reply-text" id="reply-text"
						maxlength="4000"></textarea>
					<div class="reply-buttons">
						<button class="cancel">취소</button>
						<button class="write">작성</button>
					</div>
				</div>
				<div class="replyList" id="ajax">
					<%
					for (Reply reply : replies) {
						int rNum = reply.getNum();
						int depth = new ReplyDAO().calcDepth(rNum);
					%>
					<div class="reply" style="margin-left: <%=30 * depth%>px;">
						<div id="replyNum" class="hidden"><%=rNum%></div>
						<%
						if (depth > 0) {
						%>
						<div class="indentation">└</div>
						<%
						}
						%>
						<div class="reply-head">
							<div class="reply-writer"><%=reply.getTravelerNickname()%></div>
						</div>
						<div class="reply-body">
							<p><%=reply.getText()%></p>
						</div>
						<div class="reply-tail">
							<div class="reply-time"><%=reply.getWrittenTime()%></div>
							<button class="reply-btn">답글</button>
							<button class="report-btn open-report-modal">신고</button>
						</div>
					</div>
					<%
					}
					%>
				</div>
			</section>
		</article>
		<article class="detail">
			<section class="travel-info">
				<%
				ArrayList<Location> locations = new LocationDAO().getLocations(postNum);
				for (Location location : locations) {
				%>
				<h2 class="title"><%=location.getName()%></h2>
				<div class="location"><%=(location.getCountry() + " " + location.getCity()).trim()%></div>
				<%
				}
				%>
				<div class="period"><%=post.getStartDate() + " ~ " + post.getEndDate()%></div>
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
			<!--  -->
			<%
			if (userType == 3) {
			%>
			<form>
				<input type='button' value='삭제하기'
					onclick="location.href='postDeleteAction.jsp';" />
			</form>
			<%
			}
			%>
			<!--  -->
			<section class="post-info">
				<div class="tags">
					<%
					ArrayList<Tag> tags = new TagDAO().getTags(postNum);
					for (Tag tag : tags) {
					%>
					<div class="tag"><%=tag.getName()%></div>
					<%
					}
					%>
				</div>
				<div class="written">
					<%
					User user = new User();
					user = new UserDAO().getUser(post.getTravelerNum());
					%>
					<a class="writer"><%=user.getNickname()%></a>
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