<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="post.Report"%>
<%@ page import="post.ReportDAO"%>

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
<link rel="stylesheet" href="styles/index.css" />
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
	
	int scroll = 1;
	
	%>
	<header>
		<!-- logo -->
		<div class="logo-area">
			<h1 class="logo">
				<a href="adminMain.jsp"> <span>LanTravel</span> <!-- logo image 추가 후 span에 class="hidden" 추가-->
				</a>
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
		<section id="posts">
			<div class="container">
				<div class = "row">
					<table class = "table" style = "text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th style = "background-color: #eeeeee; text=align: center;">report num</th>
							<th style = "background-color: #eeeeee; text=align: center;">type</th>
							<th style = "background-color: #eeeeee; text=align: center;">num</th>
							<th style = "background-color: #eeeeee; text=align: center;">reason</th>
							<th style = "background-color: #eeeeee; text=align: center;">writer</th>
						</tr>
					</thead>
					<tbody>
						<%
							ReportDAO reportDAO = new ReportDAO();
							ArrayList<Report> list = reportDAO.getList(scroll);
							for(int i = 0; i<list.size(); i++){
						%>
						<tr>
							<td><%= list.get(i).getReport_num() %></td>
							<td><%= list.get(i).getType() %></td>
							<td><%= list.get(i).getPr_num() %></td>
							<%if (list.get(i).getType().equals("P")) {%>
								<td><a href="post.jsp?postNum=<%= list.get(i).getPr_num() %>">
									<%= list.get(i).getReason() %></a></td>
							<%} %>
							<%if (list.get(i).getType().equals("R")){ %>
							<td><a href="reply.jsp?replyNum=<%= list.get(i).getPr_num() %>">
								<%= list.get(i).getReason() %></td> <!-- 댓글 view로 이동 -->
							<%} %>
							<td><%= list.get(i).getTraveler_num() %></td>
						</tr>
						<%
							}
						%>
					</tbody>
					</table>
				</div>
			</div>
		</section>
		<div id="padding"></div>
	</main>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
	<script src="js/bootstrap.js"></script>
</body>
</html>