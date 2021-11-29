package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;
	
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";
	
	public UserDAO() {
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
	
	public boolean loginTraveler(String id, String pw) {
//		Database db = new Database();
//		conn = db.getConnection();
//		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			sql = "SELECT num FROM traveler WHERE id = ? AND pw = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			} else {
				id = null;
				pw = null;
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
