package post;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReplyWriteServlet")
public class ReplyWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		int postNum = Integer.parseInt(request.getParameter("postNum"));
		int pNum = Integer.parseInt(request.getParameter("pNum"));
		int tNum = Integer.parseInt(request.getParameter("tNum"));
		String text = request.getParameter("text");
		new ReplyDAO().writeReply(text, tNum, pNum, postNum);
	}
}
