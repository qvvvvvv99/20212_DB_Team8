package post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TagDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public TagDAO() {
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
	
	public int getNextHashNum() {
		String sql = "SELECT NVL(MAX(tag_id), 0) FROM hashtag";
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
	
	public ArrayList<Tag> getTags(int postNum) {
		String sql = "SELECT tag_id, tag_name FROM hashtag WHERE post_num = ?";
		ArrayList<Tag> list = new ArrayList<Tag>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, postNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Tag tag = new Tag();
				tag.setId(rs.getInt(1));
				tag.setName(rs.getString(2));
				list.add(tag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void writeTag(PostDAO post, String tag) {
		String sql = "INSERT into hashtag  VALUES(?, ?, ?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, post.getNextNum()-1);
			ps.setInt(2, getNextHashNum());
			ps.setString(3, tag);
			ps.executeUpdate();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return; // DB 오류
	}
}
