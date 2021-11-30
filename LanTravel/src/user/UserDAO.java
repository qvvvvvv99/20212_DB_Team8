package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import post.Post;
import post.Tag;

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
			sql = "SELECT * FROM traveler WHERE id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getId());
			rs = ps.executeQuery();
			
			if(rs.next()) return -1; //���̵� �ߺ�
			
			sql = "SELECT * FROM traveler WHERE nickname = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getNickname());
			rs = ps.executeQuery();
			
			if(rs.next()) return -2; //�г��� �ߺ�
			
			// tuple ���� count
			sql = "SELECT COUNT(*) FROM traveler";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// tnum ����
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
	
	public User getUser(int num) {
		String sql = "SELECT id, pw, nickname, email FROM traveler WHERE num = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getString(1));
				user.setPw(rs.getString(2));
				user.setNickname(rs.getString(3));
				user.setEmail(rs.getString(4));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
