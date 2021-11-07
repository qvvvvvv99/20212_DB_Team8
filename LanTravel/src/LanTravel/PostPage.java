package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostPage {
	public static int userState;	// 사용자 상태(1:비회원 2:TRAVELER 3:ADMIN)
	Connection conn;
	Statement stmt;
	public static int pNum;
	
	public PostPage(Connection conn, Statement stmt, int userStatem, int pNum) {
		this.conn = conn;
		this.stmt = stmt;
		this.userState = userState;
		this.pNum = pNum;
		
		postView();
	}
	
	// 3. 상세화면 (포스트)
	public void postView() {
		ResultSet rs = null;
		
		try {
			String sql = "SELECT p.Start_date, p.End_date, p.Text, p.Written_time, pl.Name, pl.Country, pl.City, t.Nickname " + 
					"from post p, traveler t, post_locations pl " + 
					"where p.traveler_num = t.num " + 
					"and pl.post_num = p.post_num " + 
					"and p.Post_num = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, pNum);
			rs = ps.executeQuery();

			while (rs.next()) {
				String start_date = rs.getString(1);
				String end_date = rs.getString(2);
				String text = rs.getString(3);
				String w_time = rs.getString(4);
				String loc_name = rs.getString(5);
				String loc_country = rs.getString(6);
				String loc_city = rs.getString(7);
				String nickname = rs.getString(8);
				System.out.printf("작성자 : %s\n", nickname);
				System.out.printf("장소 : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("여행기간 : %s ~ %s\n", start_date, end_date);
				System.out.printf("작성 시간 : %s\n", w_time);
				System.out.printf("%s\n", text);
			}
			
			sql = "SELECT h.tag_name " + 
					"from post p, hashtag h " + 
					"where h.post_num = p.post_num " + 
					"and p.Post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pNum);
			rs = ps.executeQuery();
			
			System.out.print("태그 : ");
			while (rs.next()) {
				String tag = rs.getString(1);
				System.out.print("#" + tag + " ");
			}
			System.out.println();
			
			sql = "SELECT avg(r.score) "
					+ "from post p, rating r "
					+ "where r.post_num = p.post_num "
					+ "and p.post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pNum);
			rs = ps.executeQuery();
			
			rs.next();
			int score = rs.getInt(1);
			System.out.println("별점 : " + score);
			
//			if(userState == 1)
//			else if(userState == 2)

			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
