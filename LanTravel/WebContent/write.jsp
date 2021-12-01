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
				<a href="index.jsp"><img src="images/logo/logo.png"> <span class="hidden">LanTravel</span>
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

	<h2>게시물 작성</h2>

	<div class="upload-form">
		<form method="post" action="writeAction.jsp">
			<table class="uploadTable">
				<tbody>
					<tr>
						<td><textarea name="text" cols="80" rows="4"
								placeholder="게시글을 입력하세요."></textarea></td>
					</tr>
					<tr>
						<td><textarea name="hash" cols="80" rows="1"
								placeholder="해시태그 입력하세요."></textarea></td>
					</tr>
					     <tr>
				        <td>
				         <input type='file' id='btnAtt' name='image1' required/>
				         <input type='file' id='btnAtt' name='image2'/>
				        </td>
				      </tr>
				      <tr>
				        <td>
				         <input type='file' id='btnAtt' name='image3'/>
				         <input type='file' id='btnAtt' name='image4'/>
				        </td>
				      </tr>
				      <tr>
				        <td>
				         <input type='file' id='btnAtt' name='image5'/>
				          <input type='file' id='btnAtt' name='image6'/>
				         </td>
				      </tr>
					<tr>
						<td>시작날짜 &nbsp <input type="date" name="startDate">
							&nbsp &nbsp &nbsp 종료날짜 &nbsp <input type="date" name="endDate">
						</td>
					</tr>
					<tr>
						<td>국가명 &nbsp <input type="text" name="country">
							&nbsp &nbsp &nbsp 도시명 &nbsp <input type="text" name="city">
							&nbsp &nbsp &nbsp 장소명 &nbsp <input type="text" name="name"
							required>
						</td>
					</tr>
				</tbody>
			</table>
			<input type="submit" class="upload-btn" value="게시하기">
		</form>
	</div>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
	<script src="scripts/write.js"></script>
	<script type="text/javascript">
	window.onload = function(){
        target = document.getElementById('btnAtt');
        target.addEventListener('change', function(){
            fileList = "";
            for(i = 0; i < target.files.length; i++){
                fileList += target.files[i].name + '<br>';
            }
        });
    }
</script>
</body>

</html>