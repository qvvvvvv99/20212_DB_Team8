<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "user.UserDAO"%>
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
		UserDAO userDAO = new UserDAO();
		boolean result = userDAO.loginTraveler(user.getId(), user.getPw());
		if (result){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("location.href = 'index.jsp'");
			script.println("</script>");
			// 상단에 로그인 모양 없애기
		}
		else if (!result){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인에 실패하였습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
	%>
</body>
</html>