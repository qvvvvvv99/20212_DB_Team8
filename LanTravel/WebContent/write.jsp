<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.text.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<title>게시물 작성</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" />
<link rel="stylesheet"
	href="https://spoqa.github.io/spoqa-han-sans/css/SpoqaHanSans-kr.css" />
<link rel="stylesheet" href="styles/write.css">
<link href="https://fonts.googleapis.com/earlyaccess/notosanskr.css"
	rel="stylesheet">
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
				<li class="menu-item"><a href="login.jsp"><i
						class="fas fa-sign-in-alt"></i></a></li>
				<li class="menu-item"><a href="#"><i class="fas fa-heart"></i></a>
				</li>
				<li class="menu-item"><a href="user.jsp"><i class="fas fa-user"></i></a>
				</li>
				<li class="menu-item"><a href="write.jsp"><i
						class="fas fa-pen-nib"></i></a></li>
			</ul>
		</nav>
	</header>

	<h2>게시물 작성</h2>

	<div class="upload-form">
		<form>
			<table class="uploadTable">
				<tbody>
					<tr>
						<td><textarea id="content" cols="80" rows="4"
								placeholder="게시글을 입력하세요."></textarea></td>
					</tr>
					<tr>
						<td><textarea id="hash" cols="80" rows="1"
								placeholder="해시태그 입력하세요."></textarea></td>
					</tr>
					<tr>
						<td><input type='file' id='btnAtt' multiple='multiple' required/>
							<div id='att_zone'
								data-placeholder='파일을 첨부 하려면 파일 선택 버튼을 클릭하거나 파일을 드래그앤드롭 하세요'></div>
							</div></td>
					</tr>
					<tr>
						<td>시작날짜 &nbsp <input type="date" name="startDate">
							&nbsp &nbsp &nbsp 종료날짜 &nbsp <input type="date" name="endDate">
						</td>
					</tr>
					<tr>
						<td>국가명 &nbsp <input type="text" name="country">
							&nbsp &nbsp &nbsp 도시명 &nbsp <input type="text" name="city">
							&nbsp &nbsp &nbsp 장소명 &nbsp <input type="text" name="place"
							required>
						</td>
					</tr>
				</tbody>
			</table>
			<button class="upload-btn">게시하기</button>
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
	<script src="scripts/write.js"></script>
</body>

</html>