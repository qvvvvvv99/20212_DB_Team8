<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import = "post.PostDAO"%>
<%@ page import = "java.io.PrintWriter"%>
<% request.setCharacterEncoding("UTF-8");%>

<jsp:useBean id = "post" class = "post.Post" scope = "page" />
<jsp:setProperty name = "post" property = "startDate" />
<jsp:setProperty name = "post" property = "content" />
<jsp:setProperty name = "post" property = "endDate" />
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
		int userNum = 0;
		if(session.getAttribute("userNum") != null){
			userNum = (int)session.getAttribute("userNum");	
		}
		PostDAO postDAO = new PostDAO();
		String startDate = "TO_DATE(" + post.getStartDate()+", 'YYYY-MM-DD')";
		String endDate = "TO_DATE(" + post.getEndDate()+", 'YYYY-MM-DD')";
		int res = postDAO.writePost(startDate, endDate, post.getText(), userNum);
		
		// DB 오류
		if(res == -1){
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
			script.println("location.href = 'post.jsp?postNum='+post.getNum()");
			script.println("</script>");
		}
	%>
</body>
</html>