package post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LocationDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs;

	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME = "project";
	public static final String USER_PASSWD = "comp322";

	public LocationDAO() {
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
	
	public ArrayList<Location> getLocations(int postNum) {
		String sql = "SELECT name, NVL(country, ' '), NVL(city, ' ') FROM post_locations WHERE post_num = ?";
		ArrayList<Location> list = new ArrayList<Location>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, postNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				Location location = new Location();
				location.setName(rs.getString(1));
				location.setCountry(rs.getString(2));
				location.setCity(rs.getString(3));
				list.add(location);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
