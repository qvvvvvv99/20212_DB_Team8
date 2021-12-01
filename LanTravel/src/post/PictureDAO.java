package post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PictureDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public PictureDAO() {
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
	
	public ArrayList<Picture> getPictures(int postNum) {
		String sql = "SELECT pic_number, link FROM post_pictures WHERE post_num = ?";
		ArrayList<Picture> list = new ArrayList<Picture>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, postNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Picture picture = new Picture();
				picture.setPicNum(rs.getInt(1));
				picture.setLink(rs.getString(2));
				list.add(picture);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getNextPictureNum() {
		String sql = "SELECT NVL(MAX(pic_number), 0) FROM post_pictures";
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
	
	public void writePicture(PostDAO post, String fname){
		String sql = "INSERT into post_pictures VALUES(?, ?, ?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, post.getNextNum()-1);
			ps.setInt(2, getNextPictureNum());
			ps.setString(3, fname);
			ps.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return; // DB 오류
	}
}
