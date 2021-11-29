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
	<div class="register-form">
		<h1>회원 가입을 환영합니다</h1>
		<form>
			<fieldset>
				<legend>사용자 정보</legend>
				<ul>
					<li><label class="reg" for="uid">아이디 <em> * </em></label> <input
						type="text" id="uid" class="text-field" autofocus
						placeholder="4자 ~ 10자 사이, 공백없이" required></li>
					<li><label class="reg" for="pwd1">비밀번호 <em> * </em></label> <input
						type="password" id="pwd1" class="text-field"
						placeholder="문자와 숫자, 특수 기호 포함" required></li>
					<li><label class="reg" for="pw2">비밀번호 확인 <em> * </em></label>
						<input type="password" id="pwd2" class="text-field" required>
					</li>
					<li><label class="reg" for="umail">이메일 <em> * </em></label> <input
						type="email" id="umail" class="text-field" required></li>
					<li><label class="reg" for="alias">닉네임 <em> * </em></label> <input
						type="text" id="alias" class="text-field" required></li>
					<li><label class="reg" for="tel">전화번호</label> <input
						type="tel" id="tel" class="text-field"></li>
				</ul>
			</fieldset>
			<div>
				<input type="submit" value="가입하기" class="reg-btn"> <input
					type="reset" value="초기화" class="reg-btn">
			</div>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>