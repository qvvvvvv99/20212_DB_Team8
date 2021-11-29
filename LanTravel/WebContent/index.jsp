<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
				<li class="menu-item"><a href="#"><i class="fas fa-pen-nib"></i></a>
				</li>
			</ul>
		</nav>
	</header>
	<main>
		<section id="search">
			<form class="search-form" action="/index.html" method="get">
				<div class="icon">
					<i class="fas fa-search"></i>
				</div>
				<input class="search-box" type="search" name="search"
					placeholder="검색" aria-label="Search" />
				<div class="divider"></div>
				<div class="dropdown">
					<div class="dropdown-button">
						<div class="search-type">장소</div>
						<i class="fas fa-angle-down"></i>
					</div>
					<div class="dropdown-options">
						<input type="radio" name="sType" id="location" value="location"
							checked /> <label for="location" class="option-item">장소</label>
						<input type="radio" name="sType" id="writer" value="writer" /> <label
							for="writer" class="option-item">작성자</label> <input type="radio"
							name="sType" id="opt3" value="opt3" /> <label for="opt3"
							class="option-item">opt3</label>
					</div>
				</div>
			</form>
		</section>
		<section id="posts">
			align
			<ol class="container">
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
				<li class="box"><img class="thumbnail" />
					<div class="details">
						<span class="title">Title</span>
						<div class="cnt">
							<div class="fav-cnt">
								<i class="fas fa-heart"></i> <span>5k</span>
							</div>
							<div class="view-cnt">
								<i class="fas fa-eye"></i> <span>58.1k</span>
							</div>
						</div>
					</div></li>
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