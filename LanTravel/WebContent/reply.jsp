<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="user.User"%>
<%@ page import="user.UserDAO"%>
<%@ page import="post.Post"%>
<%@ page import="post.PostDAO"%>
<%@ page import="post.Reply"%>
<%@ page import="post.ReplyDAO"%>
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
<link rel="stylesheet" 
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" 
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" 
	crossorigin="anonymous">
<link rel="stylesheet" href="styles/index.css" />
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
				<%if (userType == 3) {%>
				<a href="adminMain.jsp"> <span>LanTravel</span> <!-- logo image 추가 후 span에 class="hidden" 추가-->
				</a>
				<%}else{ %>
				<a href="index.jsp"> <span>LanTravel</span> <!-- logo image 추가 후 span에 class="hidden" 추가-->
				</a>
				<%} %>
			</h1>
		</div>
		<!-- menu -->
		<nav>
			<ul class="menu">
				<li class="menu-item hidden"><a href="login.jsp"><i
						class="fas fa-sign-in-alt"></i></a></li>
				<li class="menu-item hidden"><a href="#"><i class="fas fa-heart"></i></a>
				</li>
				<li class="menu-item"><a href="user.jsp"><i
						class="fas fa-user"></i></a></li>
				<li class="menu-item hidden"><a href="write.jsp"><i
						class="fas fa-pen-nib"></i></a></li>
			</ul>
		</nav>
	</header>
	<main>
		<%
		if (request.getParameter("replyNum") == null) {
			out.println("<script>location.href = 'adminMain.jsp';</script>");
			return;
		}
		int replyNum = Integer.parseInt(request.getParameter("replyNum"));
		if (replyNum == 0) {
			out.println("<script>location.href = 'adminMain.jsp';</script>");
			return;
		}
		session.setAttribute("replyNum", replyNum);
		ReplyDAO replyDao = new ReplyDAO();
		Reply reply = replyDao.getReply(replyNum);
		%>
		<div class="container">
			<div class = "row">
				<table class = "table table-striped" style = "text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th style="text-align: center; background-color: #eeeeee ;">댓글 상세보기</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style = "width: 30%;">댓글 번호</td>
							<td colspan = "2"><%=reply.getNum() %></td>
						</tr>
						<tr>
							<td style = "width: 30%;">작성자 번호</td>
							<td colspan = "2"><%=reply.getTravelerNum() %></td>
						</tr>
						<tr>
							<td style = "width: 30%;">작성일</td>
							<td colspan = "2"><%=reply.getWrittenTime() %></td>
						</tr>
						<tr>
							<td style = "width: 30%;">본문</td>
							<td colspan = "2"><%=reply.getText() %></td>
						</tr>
					</tbody>
					</table>
				<a href = "replyDeleteAction.jsp" class = "btn btn-primary">삭제</a>
			</div>
		</article>
		<div id="padding"></div>
	</main>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>