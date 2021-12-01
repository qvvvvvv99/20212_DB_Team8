package post;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReplyServlet")
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String replyNum = request.getParameter("replyNum");
		response.getWriter().write(getJSON(replyNum));
	}

	public String getJSON(String postNum) {
		int pNum = 0;
		if (postNum == null) {
			pNum = Integer.parseInt(postNum);
		}
		StringBuffer result = new StringBuffer("");
		result.append("{\result\":[");
		ReplyDAO replyDao = new ReplyDAO();
		ArrayList<Reply> replyList = replyDao.getList(pNum);
		for (Reply reply : replyList) {
			result.append("[{\"value\": \"" + reply.getNum() + "\"},");
			result.append("{\"value\": \"" + reply.getParentNum() + "\"},");
			result.append("{\"value\": \"" + reply.getText() + "\"},");
			result.append("{\"value\": \"" + reply.getTravelerNum() + "\"},");
			result.append("{\"value\": \"" + reply.getWrittenTime() + "\"}],");
		}
		result.append("]}");
		return result.toString();
	}
}
