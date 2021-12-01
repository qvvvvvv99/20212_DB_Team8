<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "user.UserDAO"%>
<%@ page import = "user.User"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>회원 정보</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
<link rel="stylesheet"
	href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
<link rel="stylesheet" href="styles/user.css" />
<link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css"
	rel="stylesheet" />
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");

		int userType = 1; // 1: Guest, 2: Traveler, 3: Admin
		String id = null;
		int Tnum = 0;
		
		if (session.getAttribute("userType") != null) {
			userType = (int) session.getAttribute("userType");
		}
		
		if (session.getAttribute("id") != null) {
			id = (String) session.getAttribute("id");
		}
		if(session.getAttribute("Tnum") != null){
			Tnum = (int)session.getAttribute("Tnum");
		}
	%>
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
			</ul>
		</nav>
	</header>
	<div class="update-form">
		<h1>회원정보</h1>
		<form>
			<fieldset>
				<table class="updateTable">
				<% 
					String nickname;
					String pw;
					String email;
					
					UserDAO userdao = new UserDAO();
					User user = null;
					user = userdao.getUser(Tnum);
					
					nickname = user.getNickname();
					pw = user.getPw();
					email = user.getEmail();
				%>
					<tr>
						<th>아이디</th>
						<td><%=id %></td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td><%=nickname %></td>
					</tr>
					<tr>
						<th>패스워드</th>
						<td><%=pw %></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td><%=email %></td>
					</tr>
				</table>
			</fieldset>
			<div>
				<button type="button" class="update-btn" onclick="location.href='userUpdate.jsp'">
				회원정보 수정하기</button>
			</div>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>