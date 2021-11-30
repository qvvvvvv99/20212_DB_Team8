<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "user.User"%>
<%@ page import = "user.UserDAO"%>
<%@ page import = "java.io.PrintWriter"%>
<% request.setCharacterEncoding("UTF-8");%>

<!DOCTYPE html>
<html>
<head>
<title>회원정보수정</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<body>
	<%
		String id = null;
		if(session.getAttribute("id") != null){
			id = (String) session.getAttribute("id");
		}
		
		request.setCharacterEncoding("euc-kr");
		String pw = request.getParameter("pw");
		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		
		User user = new User(id, pw, email, nickname);
		
		UserDAO userDAO = new UserDAO();
		int result = userDAO.updateTraveler(user);
		if (result == 1){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('회원정보수정이 완료되었습니다.')");
			script.println("location.href = 'user.jsp'");
			script.println("</script>");
		}
		else if(result == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('회원정보수정에 실패하였습니다.')");
			script.println("location.href = 'userUpdate.jsp'");
			script.println("</script>");
		}
		else if(result == -1){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 존재하는 닉네임이거나, 기존의 닉네임입니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
	%>
	
</body>
</html>