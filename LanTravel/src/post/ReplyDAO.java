package post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReplyDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public ReplyDAO() {
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
		String sql = "SELECT NVL(MAX(reply_num), 0) FROM reply";
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
	
	public ArrayList<Reply> getReplies(int postNum) {
		String sql = "SELECT r.reply_num, r.text, r.written_time, r.traveler_num, t.nickname, NVL(r.p_reply_num, -1) FROM reply r, traveler t WHERE r.post_num = ? AND r.traveler_num = t.num ORDER BY r.reply_num";
		ArrayList<Reply> list = new ArrayList<Reply>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, postNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				// reply_num, text, written_time, traveler_num, p_reply_num
				int rNum = rs.getInt(1);
				Reply reply = new Reply(rNum, rs.getString(2), rs.getDate(3), rs.getInt(4), rs.getString(5), rs.getInt(6));
				list.add(reply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int calcDepth(int num) {
		String sql = "SELECT level FROM reply where reply_num = ? START WITH p_reply_num IS NULL CONNECT BY PRIOR reply_num = p_reply_num";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) - 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
}
