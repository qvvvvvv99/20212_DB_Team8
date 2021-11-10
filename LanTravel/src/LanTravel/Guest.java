package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Guest {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;
	
	private String id;
	private String pw;
	private int userType; // (1: guest), 2: traveler, 3: admin
	private int num;

	public Guest() {
		this.conn = null;
		this.stmt = null;
		id = null;
		pw = null;
		userType = 1;
		num = -1;
	}

	public String getId() {
		return id;
	}

	public String getPw() {
		return pw;
	}

	public int getUserType() {
		return userType;
	}

	public int getNum() {
		return num;
	}

	public void join(String id, String pw, String nickname, String email) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int tnum = -1;

		try {
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
			ps.setString(2, id);
			ps.setString(3, pw);
			ps.setString(4, nickname);
			ps.setString(5, email);
			rs = ps.executeQuery();

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean loginTraveler(String id, String pw) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
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
				num = rs.getInt(1);
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

	public boolean loginAdmin(String id, String pw) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT num FROM admin WHERE id = ? AND pw = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();

			if (rs.next()) {
				num = rs.getInt(1);
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