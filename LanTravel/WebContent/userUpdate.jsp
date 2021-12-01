<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "java.io.PrintWriter"%>
<%@ page import = "user.*"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>회원 정보수정</title>
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
	
		if (session.getAttribute("userType") != null) {
			userType = (int) session.getAttribute("userType");
		}
		
		if (session.getAttribute("id") != null) {
			id = (String) session.getAttribute("id");
		}
		int Tnum = 0;
		if(session.getAttribute("Tnum") != null){
			Tnum = (int)session.getAttribute("Tnum");
		}
	%>
	
	<%
		if (session.getAttribute("id") == null) { 
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('세션이 만료되었습니다.')");
			script.println("location.href = 'index.jsp'");
			script.println("</script>");
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
	<div class="update-form">
		<h1>회원 정보 수정하기</h1>
		<form method = "post" action = "userUpdateAction.jsp">
			<fieldset>
				<legend>사용자 정보</legend>
				<table class="updateTable">
				<%if(userType==2){%>
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
						<td><input type="text" value="<%=nickname %>" name="nickname" required /></td>
					</tr>
					<tr>
						<th>패스워드</th>
						<td><input type="password" value="<%=pw %>" name="pw" required /></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td><input type="email" value="<%=email %>" name="email" required /></td>
					</tr>
					<% }%>
					<%if(userType==3){%>
				<% 
					String pw;
					
					UserDAO userdao = new UserDAO();
					User user = null;
					user = userdao.getAdmin(id);

					pw = user.getPw();
				%>
					<tr>
						<th>아이디</th>
						<td><%=id %></td>
					</tr>
					<tr>
						<th>패스워드</th>
						<td><input type="password" value="<%=pw %>" name="pw" required /></td>
					</tr>
					<% }%>
				</table>
			</fieldset>
			<div>
				<button class="update-btn">수정하기</button>
				<button type = "button" onclick="location.href='user.jsp'"
					class="update-btn">돌아가기</button>
			</div>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>