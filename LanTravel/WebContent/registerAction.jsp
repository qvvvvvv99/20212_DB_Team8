<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import = "user.UserDAO"%>
<%@ page import = "java.io.PrintWriter"%>
<% request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id = "user" class = "user.User" scope = "page" />
<jsp:setProperty name = "user" property = "id" />
<jsp:setProperty name = "user" property = "pw" />
<jsp:setProperty name = "user" property = "pwc" />
<jsp:setProperty name = "user" property = "email" />
<jsp:setProperty name = "user" property = "nickname" />
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<body>
	<%
		String id = null;
		if(session.getAttribute("id") != null){
			id = (String) session.getAttribute("id");
		}
		if(id != null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인이 되어있습니다.')");
			script.println("location.href = 'index.jsp'");
			script.println("</script>");
		}
		if(!user.getPw().equals(user.getPwc())) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호를 다시 확인해 주세요.')");
			script.println("history.back()");
			script.println("</script>");
		}else{
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			if (result == 1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'index.jsp'");
				script.println("</script>");
			}
			else if (result == 0){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('회원가입에 실패하였습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else if (result == -1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else if (result == -2){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 닉네임입니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
		}
		
	%>
</body>
</html>