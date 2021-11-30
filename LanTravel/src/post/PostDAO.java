package post;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PostDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public PostDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loading : Success!");
		} catch (ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		try {
			conn = DriverManager.getConnection(URL, USER_NAME, USER_PASSWD);
			System.out.println("Oracle Connected.");
			stmt = conn.createStatement();
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getLocalizedMessage());
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}
	}

	public String getCurrTime() {
		String sql = "SELECT NOW()";
		try {
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // DB 오류
	}

	public int getNextNum() {
		String sql = "SELECT NVL(MAX(post_num), 0) FROM post";
		try {
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}

	public ArrayList<Post> getList(int scroll) {
		final int limit = 30;
		String sql = "SELECT * FROM ( SELECT post_num, view_count, bookmark_count FROM post WHERE post_num < ? ORDER BY post_num DESC ) WHERE ROWNUM <= ?";
		ArrayList<Post> list = new ArrayList<Post>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNextNum() - (scroll - 1) * limit);
			ps.setInt(2, limit);
			System.out.println(limit);
			rs = ps.executeQuery();
			while (rs.next()) {
				// post_num, view_count, bookmark_count
				Post post = new Post(rs.getInt(1), rs.getInt(2), rs.getInt(3));
				list.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Post getPost(int num) {
		String sql = "SELECT * FROM post WHERE post_num = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				// num, startDate, endDate, text, writtenTime,
				// travelerNum, viewCnt, bookmarkCnt
				Post post = new Post(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getString(4), rs.getDate(5),
						rs.getInt(6), rs.getInt(7), rs.getInt(8));
				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int increaseViewCnt(int num) {
		int viewCount = 0;
		try {
			// 조회 수 추출
			String sql = "SELECT view_count FROM post WHERE post_num = ? FOR UPDATE WAIT 5";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				viewCount = rs.getInt(1) + 1;
			}
			
			// 조회 수 증가
			sql = "UPDATE post SET view_count = ? WHERE post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, viewCount);
			ps.setInt(2, num);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}

	public int increaseFavoriteCnt(int num) {
		String sql = "UPDATE post SET favorite_count = favorite_count + 1 WHERE post_num = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}

	public int decreaseFavoriteCnt(int num) {
		String sql = "UPDATE post SET favorite_count = favorite_count - 1 WHERE post_num = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	// TODO: 작성 필요
	public int writePost(String startDate, String endDate, String text, int tNum) {
		String sql = "INSERT into post VALUE(?, ?, ?, ?, ?, ?, 0, 0)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNextNum());
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ps.setString(4, text);
			ps.setString(5, getCurrTime());
			ps.setInt(6, tNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
}
