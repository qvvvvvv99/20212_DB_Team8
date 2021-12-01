<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "post.PostDAO"%>
<%@ page import = "java.io.PrintWriter"%>
<% request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id = "user" class = "user.User" scope = "page" />
<jsp:setProperty name = "user" property = "id" />
<jsp:setProperty name = "user" property = "pw" />
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<body>
	<%
		request.setCharacterEncoding("euc-kr");
		String status = request.getParameter("status");
		
		int Pnum = (int)session.getAttribute("postNum");
	
		PostDAO postDAO = new PostDAO();
		
		if(postDAO.deletePost(Pnum)==1){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('게시물이 삭제되었습니다.')");
			script.println("location.href = 'adminMain.jsp'");
			script.println("</script>");
		}else{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('게시물 삭제에 실패하였습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
		
	%>
</body>
</html>