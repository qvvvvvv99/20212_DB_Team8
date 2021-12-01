<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import = "post.PostDAO"%>
<%@ page import = "post.LocationDAO"%>
<%@ page import = "post.TagDAO"%>
<%@ page import = "java.io.PrintWriter"%>
<% request.setCharacterEncoding("UTF-8");%>

<jsp:useBean id = "post" class = "post.Post" scope = "page" />
<jsp:setProperty name = "post" property = "text" />
<jsp:useBean id = "location" class = "post.Location" scope = "page" />
<jsp:setProperty name = "location" property = "country" /> 
<jsp:setProperty name = "location" property = "city" /> 
<jsp:setProperty name = "location" property = "name" /> 
<!DOCTYPE html>
<html>
<head>
<title>게시물 작성</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");
	
		// 세션에 값이 담겨있는지 체크
		int Tnum = 0;
		if(session.getAttribute("Tnum") != null){
			Tnum = (int)session.getAttribute("Tnum");
		}
		PostDAO postDAO = new PostDAO();
		String sDate = request.getParameter("startDate");
		String eDate = request.getParameter("endDate");
		int res = postDAO.writePost(sDate, eDate, post.getText(), Tnum);
		
		LocationDAO locationDAO = new LocationDAO();
		int resLocation = locationDAO.writePostLocation(postDAO, location.getName(), location.getCountry(), location.getCity());
		
		String allhash = request.getParameter("hash");
		if(allhash != null){
			 String[] hashAry = allhash.split(" ") ;
			 for(int i=0; i < hashAry.length; i++){
				 TagDAO tagDAO = new TagDAO();
				 hashAry[i] = hashAry[i].replace("#", "");
		         tagDAO.writeTag(postDAO, hashAry[i]);
		     }
		}
		
		// DB 오류
		if(res == -1 || resLocation == -1){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('게시글 작성에 실패하였습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
		// 글쓰기가 정상적으로 실행되면 알림창을 띄우고 생성한 Post 화면으로 이동
		else {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('게시글 작성 성공')");
			script.println("location.href = 'index.jsp'");
			script.println("</script>");
		}
	%>
</body>
</html>