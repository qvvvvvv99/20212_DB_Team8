<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="post.Post"%>
<%@ page import="post.PostDAO"%>
<%@ page import="post.Location"%>
<%@ page import="post.LocationDAO"%>
<%@ page import="post.Picture"%>
<%@ page import="post.PictureDAO"%>
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
			<% if(userType==3) {%>
				<li class="menu-item"><a href="user.jsp"><i class="fas fa-user"></i></a>
				</li>
			<%} %>
			</ul>
		</nav>
	</header>
	<main>
		<section id="search" style="background-image: url('<%= new PictureDAO().getPictures((int)(Math.random() * 20 + 80)).get(0).getLink() %>')">
			<form class="search-form" action="searchPost.jsp" method="get">
				<div class="icon">
					<i class="fas fa-search"></i>
				</div>
				<input class="search-box" type="search" name="search"
					placeholder="??????" aria-label="Search" />
				<div class="divider"></div>
				<div class="dropdown">
					<div class="dropdown-button">
						<div class="search-type">??????</div>
						<i class="fas fa-angle-down"></i>
					</div>
					<div class="dropdown-options">
						<input type="radio" name="sType" id="location" value="location"
							checked /> <label for="location" class="option-item">??????</label>
						<input type="radio" name="sType" id="writer" value="writer" /> <label
							for="writer" class="option-item">?????????</label>
					</div>
				</div>
			</form>
		</section>
		<section id="posts">
			<div id="scroll" class="hidden">1</div>
			<ol class="container" id="ajax">
				<%
				int scroll = 1;
				ArrayList<Post> postList = new PostDAO().getList(scroll);
				for (Post post : postList) {
					int postNum = post.getNum();
					ArrayList<Picture> pictures = new PictureDAO().getPictures(postNum);
				%>
				<li class="box">
					<a href="post.jsp?postNum=<%= postNum %>">
						<img class="thumbnail" src=<%= (pictures.size() > 0) ? pictures.get(0).getLink() : "" %> />
					</a>
					<div class="details">
						<span class="title">
					<%
					ArrayList<Location> locations = new LocationDAO().getLocations(postNum);
					int idx = 0;
					int lastIdx = locations.size() - 1;
					for (Location location : locations) {
					%>
						<%= location.getName() + ((idx < lastIdx) ? ", " : "") %>
					<%
						idx++;
					}
					%>
						</span> <!-- TODO: getLocName ?????? > getNum ?????? -->
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span><%= post.getBookmarkCnt() %></span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span><%= post.getViewCnt() %></span>
							</div>
						</div>
					</div>
				</li>
				<%
				}
				%>
			</ol>
		</section>
		<div id="padding"></div>
	</main>
	<footer>
		<p>Database(COMP322005) Team8 &copy; 2021</p>
	</footer>
	<script src="scripts/index.js"></script>
</body>
</html>