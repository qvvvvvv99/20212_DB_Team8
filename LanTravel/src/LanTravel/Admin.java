package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;
	private int num;

	public Admin(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		num = 0;
	}

	public void setNum(int Anum) {
		this.num = Anum;
	}

	// ��й�ȣ ����
	public boolean updatePassword(String pw) {
//		Database db = new Database();
//		conn = db.getConnection();
//		stmt = db.getStatement();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;

		try {
			sql = "UPDATE admin SET pw = ? WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, pw);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			ps.close();
			rs.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void DeletePostReply(String type, int num) {
//		Database db = new Database();
//		conn = db.getConnection();
//		stmt = db.getStatement();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String sql = null;

		try {
			switch (type) {
			case "P":
				
				conn.setAutoCommit(false);

				sql = "DELETE FROM reply WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "DELETE FROM rating WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "DELETE FROM post_pictures WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "DELETE FROM post_locations WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "DELETE FROM hashtag WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "select report_num from record WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
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

				sql = "DELETE FROM post WHERE post_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				conn.commit();

				System.out.println("����Ʈ�� �����Ǿ����ϴ�.");
			case "R":
				sql = "select report_num from record WHERE reply_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();

				rnum = 0;

				while (rs.next()) {
					rnum = rs.getInt(1);
					
					conn.setAutoCommit(false);

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
				ps.setInt(1, num);
				rs = ps.executeQuery();

				sql = "DELETE FROM reply WHERE reply_num = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				conn.commit();

				System.out.println("����� �����Ǿ����ϴ�.");
			}

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
		}
	}
}
