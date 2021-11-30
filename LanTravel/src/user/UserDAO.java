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
	
	public int join(User user) {

		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int tnum = -1;

		try {
			sql = "SELECT * FROM traveler WHERE id = ? for update wait 5";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getId());
			rs = ps.executeQuery();
			
			if(rs.next()) return -1; //아이디 중복
			
			sql = "SELECT * FROM traveler WHERE nickname = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getNickname());
			rs = ps.executeQuery();
			
			if(rs.next()) return -2; //닉네임 중복
			
			// tuple 개수 count
			sql = "SELECT COUNT(*) FROM traveler";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// tnum 지정
			if (rs.next()) {
				tnum = rs.getInt(1) + 1;
			}
			
			// input
			sql = "INSERT INTO traveler VALUES(?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, tnum);
			ps.setString(2, user.getId());
			ps.setString(3, user.getPw());
			ps.setString(4, user.getNickname());
			ps.setString(5, user.getEmail());
			rs = ps.executeQuery();

			ps.close();
			rs.close();
			
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public User getUser(String id) {	
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		User user = null;
		
		try {
			sql = "SELECT id, pw, email, nickname FROM traveler WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public int updateTraveler(User user) {	
		System.out.println(user.getId());
		System.out.println(user.getPw());
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT nickname FROM traveler WHERE nickname = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getNickname());
			rs = ps.executeQuery();

			if (rs.next()) {
				return -1;
			}
			
			sql = "UPDATE traveler SET pw = ?, email = ?, nickname = ? WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getPw());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getNickname());
			ps.setString(4, user.getId());
			rs = ps.executeQuery();

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
