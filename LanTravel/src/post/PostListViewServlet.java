package post;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PostListViewServlet")
public class PostListViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String scroll = request.getParameter("scroll");
		response.getWriter().write(getJSON(scroll));
	}

	public String getJSON(String scroll) {
		int scr = 0;
		if (scroll != null) {
			scr = Integer.parseInt(scroll);
		}
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ArrayList<Post> posts = new PostDAO().getList(scr);
		for (Post post : posts) {
			int postNum = post.getNum();
			ArrayList<Picture> pictures = new PictureDAO().getPictures(postNum);
			String thumbnailSrc = (pictures.size() > 0) ? pictures.get(0).getLink() : "";
			ArrayList<Location> locations = new LocationDAO().getLocations(postNum);
			String title = "";
			int idx = 0;
			int lastIdx = locations.size() - 1;
			for (Location location : locations) {
				title += location.getName() + ((idx < lastIdx) ? ", " : "");
				idx++;
			}
			
			result.append("[{\"value\": \"" + postNum + "\"},");
			result.append("{\"value\": \"" + post.getViewCnt() + "\"},");
			result.append("{\"value\": \"" + post.getBookmarkCnt() + "\"},");
			result.append("{\"value\": \"" + title + "\"},");
			result.append("{\"value\": \"" + thumbnailSrc + "\"}],");
		}
		result.append("]}");
		return result.toString();
	}
}