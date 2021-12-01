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
		return null; // DB �삤瑜�
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
		return -1; // DB �삤瑜�
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
	

	public Reply getReply(int replyNum) {
		String sql = "SELECT text, written_time, traveler_num, p_reply_num, post_num FROM reply WHERE reply_num = ?";
		
		Reply reply = null;
		int tnum;
		String nickname = null;
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, replyNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				// reply_num, text, written_time, traveler_num, p_reply_num
				tnum = rs.getInt(3);
				sql = "SELECT nickname FROM traveler WHERE num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, tnum);
				ResultSet rs2 = ps.executeQuery();
				
				if(rs2.next()) {
					nickname = rs2.getString(1);
				}
				
				reply = new Reply(replyNum, rs.getString(1), rs.getDate(2), rs.getInt(3), nickname, rs.getInt(4), 0);
			}
			reply.setPostNum(rs.getInt(5));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reply;
	}
	
	public int deleteReply(int replyNum) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sql = null;

		try {
			conn.setAutoCommit(false);
			sql = "select * from record where reply_num in (select reply_num from reply where p_reply_num = ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, replyNum);
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
				sql = "select report_num from record WHERE reply_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, replyNum);
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

				sql = "DELETE FROM reply WHERE p_reply_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, replyNum);
				rs = ps.executeQuery();

				sql = "DELETE FROM reply WHERE reply_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, replyNum);
				rs = ps.executeQuery();
				
				conn.commit();

				return 1;
			

		} catch (Throwable e) {
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
		}return 0;
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
