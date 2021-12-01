package post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReportDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public ReportDAO() {
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
	
	public int getNextNum() {
		String sql = "SELECT NVL(MAX(report_num), 0) FROM report";
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
	
	public ArrayList<Report> getList(int scroll) {
		ResultSet rs2;
		final int limit = 10;
		String sql = "SELECT * FROM ( SELECT report_num, type, reason, traveler_num FROM report WHERE report_num < ? ORDER BY report_num DESC ) WHERE ROWNUM <= ?";
		ArrayList<Report> list = new ArrayList<Report>();
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNextNum() - (scroll - 1) * limit);
			ps.setInt(2, limit);
			rs = ps.executeQuery();
			while (rs.next()) {
				// post_num, view_count, bookmark_count
				Report report = new Report(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
				
				if(rs.getString(2).equals("P")) {
					sql = "SELECT post_num FROM record where report_num = ?";
					PreparedStatement ps2 = conn.prepareStatement(sql);
					ps2.setInt(1, rs.getInt(1));
					rs2 = ps2.executeQuery();
					
					if(rs2.next()) {
						report.setPr_num(rs2.getInt(1));
					}
				}else if(rs.getString(2).equals("R")) {
					sql = "SELECT reply_num FROM record where report_num = ?";
					PreparedStatement ps2 = conn.prepareStatement(sql);
					ps2.setInt(1, rs.getInt(1));
					rs2 = ps2.executeQuery();
					
					if(rs2.next()) {
						report.setPr_num(rs2.getInt(1));
					}
				}
				
				list.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
