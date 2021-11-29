<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
<link rel="stylesheet"
	href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
<link rel="stylesheet" href="styles/login.css">
<link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css"
	rel="stylesheet">
</head>
<body>
	<header>
		<!-- logo -->
		<div class="logo-area">
			<h1 class="logo">
				<a href="index.html"> <span>LanTravel</span> <!-- logo image 추가 후 span에 class="hidden" 추가-->
				</a>
			</h1>
		</div>
		<!-- menu -->
		<nav>
			<ul class="menu">
				<li class="menu-item"><a href="login.html"><i
						class="fas fa-sign-in-alt"></i></a></li>
				<li class="menu-item"><a href="#"><i class="fas fa-heart"></i></a>
				</li>
				<li class="menu-item"><a href="#"><i class="fas fa-user"></i></a>
				</li>
				<li class="menu-item"><a href="upload_post.html"><i
						class="fas fa-pen-nib"></i></a></li>
			</ul>
		</nav>
	</header>
	<div class="login-form">
		<form>
			<input type="text" name="id" class="text-field" placeholder="아이디">
			<input type="password" name="password" class="text-field"
				placeholder="비밀번호"> <input type="submit" value="로그인"
				class="submit-btn">
		</form>

		<div class="links">
			<a href="register.html">회원가입</a>
		</div>

	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>