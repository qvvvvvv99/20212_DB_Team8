<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<div class="update-form">
		<h1>회원 정보 수정하기</h1>
		<form>
			<fieldset>
				<legend>사용자 정보</legend>
				<table class="updateTable">
					<tr>
						<th>아이디</th>
						<td>아이디</td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td><input type="text" value="닉네임" name="alias" required /></td>
					</tr>
					<tr>
						<th>패스워드</th>
						<td><input type="password" value="패스워드" name="pass1" required /></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td><input type="email" value="이메일" name="email" required /></td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td><input type="tel" value="전화번호" name="tel" required /></td>
					</tr>
				</table>
			</fieldset>
			<div>
				<input type="submit" value="수정하기" class="update-btn" />
				<button type="button" onclick="location.href='login.html'"
					class="update-btn">돌아가기</button>
			</div>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
</body>
</html>