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
		String sql = "SELECT SYSDATE FROM DUAL";
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
		String sql = "SELECT * FROM ( SELECT post_num, view_count, bookmark_count FROM post WHERE post_num < ? ORDER BY post_num DESC ) WHERE ROWNUM BETWEEN 1 AND ?";
		ArrayList<Post> list = new ArrayList<Post>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNextNum() - (scroll - 1) * limit);
			ps.setInt(2, limit);
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
	
	public int writePost(String startDate, String endDate, String text, int tNum) {
		String sql = "INSERT into post VALUES(?, TO_DATE(?, 'yyyy-mm-dd'), TO_DATE(?, 'yyyy-mm-dd'), ?, TO_DATE(?, 'yyyy-mm-dd hh24:mi:ss'), ?, 0, 0)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNextNum());
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ps.setString(4, text);
			ps.setString(5, getCurrTime());
			ps.setInt(6, tNum);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	public int deletePost(int Pnum) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sql = null;

		try {
			conn.setAutoCommit(false);
			sql = "select report_num from record WHERE reply_num in (select reply_num from reply where post_num = ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Pnum);
			rs = ps.executeQuery();

			int rnum = 0;

			while (rs.next()) {
				rnum = rs.getInt(1);

				sql = "DELETE FROM record WHERE report_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rnum);
				rs1 = ps.executeQuery();

				sql = "DELETE FROM report WHERE report_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rnum);
				rs2 = ps.executeQuery();
			}
				
				
				sql = "DELETE FROM reply WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				sql = "DELETE FROM rating WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				sql = "DELETE FROM post_pictures WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				sql = "DELETE FROM post_locations WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				sql = "DELETE FROM hashtag WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				sql = "select report_num from record WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();

				rnum = 0;

				while (rs.next()) {
					rnum = rs.getInt(1);

					sql = "DELETE FROM record WHERE report_num = ?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, rnum);
					rs1 = ps.executeQuery();

					sql = "DELETE FROM report WHERE report_num = ?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, rnum);
					rs2 = ps.executeQuery();
				}

				sql = "DELETE FROM post WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Pnum);
				rs = ps.executeQuery();
				
				conn.commit();

				return 1;
		}catch (Throwable e) {
			if(conn != null) {
				try {
					conn.rollback();
				}catch (SQLException e1) {
					e1.printStackTrace();
				}
			}e.printStackTrace();
		}finally {
			try {
				conn.setAutoCommit(true);
			}catch(SQLException e2) {
				e2.printStackTrace();
			}
		} return 0;
	}
	
	public ArrayList<Post> getSearchList(int scroll, String search, String sType) {
		final int limit = 30;
		ArrayList<Post> list = new ArrayList<Post>();
		switch(sType) {
		case "location":
			String sql = "SELECT * FROM ( SELECT post_num, view_count, bookmark_count FROM post WHERE post_num < ? AND text like ? ORDER BY post_num DESC ) WHERE ROWNUM <= ?";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, getNextNum() - (scroll - 1) * limit);
				ps.setString(2, "%" + search + "%");
				ps.setInt(3, limit);
				rs = ps.executeQuery();
				while (rs.next()) {
					// post_num, view_count, bookmark_count 
					Post post = new Post(rs.getInt(1), rs.getInt(2), rs.getInt(3));
					list.add(post);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "writer":
			sql = "SELECT * FROM ( SELECT post_num, view_count, bookmark_count FROM post WHERE post_num < ? AND traveler_num in (SELECT num FROM traveler WHERE nickname LIKE ?) ORDER BY post_num DESC ) WHERE ROWNUM <= ?";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, getNextNum() - (scroll - 1) * limit);
				ps.setString(2, "%" + search + "%");
				ps.setInt(3, limit);
				rs = ps.executeQuery();
				while (rs.next()) {
					// post_num, view_count, bookmark_count
					Post post = new Post(rs.getInt(1), rs.getInt(2), rs.getInt(3));
					list.add(post);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}

		return list;		
	}
}
