package post;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReplyViewServlet")
public class ReplyViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String postNum = request.getParameter("postNum");
		response.getWriter().write(getJSON(postNum));
	}

	public String getJSON(String postNum) {
		int pNum = 0;
		if (postNum != null) {
			pNum = Integer.parseInt(postNum);
		}
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ReplyDAO replyDao = new ReplyDAO();
		ArrayList<Reply> replies = replyDao.getReplies(pNum);
		for (Reply reply : replies) {
			result.append("[{\"value\": \"" + reply.getNum() + "\"},");
			result.append("{\"value\": \"" + reply.getParentNum() + "\"},");
			result.append("{\"value\": \"" + reply.getText() + "\"},");
			result.append("{\"value\": \"" + reply.getTravelerNickname() + "\"},");
			result.append("{\"value\": \"" + reply.getWrittenTime() + "\"},");
			result.append("{\"value\": \"" + replyDao.calcDepth(reply.getNum()) + "\"}],");
		}
		result.append("]}");
		return result.toString();
	}
}
