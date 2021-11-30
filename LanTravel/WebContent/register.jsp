<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
<link rel="stylesheet"
	href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
<link rel="stylesheet" href="styles/register.css">
<link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css"
	rel="stylesheet">
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
	<div class="register-form">
		<h1>회원 가입을 환영합니다</h1>
		<form method = "post" action = "registerAction.jsp">
			<fieldset>
				<legend>사용자 정보</legend>
				<ul>
					<li><label class="reg" for="id">아이디 <em> * </em></label> <input
						type="text" name="id" class="text-field" autofocus
						placeholder="4자 ~ 10자 사이, 공백없이" required></li>
					<li><label class="reg" for="pw">비밀번호 <em> * </em></label> <input
						type="password" name="pw" class="text-field"
						placeholder="문자와 숫자, 특수 기호 포함" required></li>
					<li><label class="reg" for="pwc">비밀번호 확인 <em> * </em></label>
						<input type="password" name="pwc" class="text-field" required>
					</li>
					<li><label class="reg" for="email">이메일 <em> * </em></label> <input
						type="email" name="email" class="text-field" required></li>
					<li><label class="reg" for="nickname">닉네임 <em> * </em></label> <input
						type="text" name="nickname" class="text-field" required></li>
					<li><label class="reg" for="tel">전화번호</label> <input
						type="tel" name="tel" class="text-field"></li>
				</ul>
			</fieldset>
			<div>
				<button class="reg-btn">가입하기</button> 
				<input type="reset" value="초기화" class="reg-btn">
			</div>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>