package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostPage {
	public static int userState;	// 사용자 상태(1:비회원 2:TRAVELER 3:ADMIN 4:작성자)
	Connection conn;
	Statement stmt;
	public static int pNum;
	int num;
	Scanner sc = new Scanner(System.in);
	
	public PostPage(Connection conn, Statement stmt, int userState, int pNum) {
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
			String sql = "SELECT p.Start_date, p.End_date, p.Text, p.Written_time, pl.Name, pl.Country, pl.City, t.Nickname, t.num " + 
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
				int tnum = rs.getInt(9);	// 작성자인지 확인 용도
				System.out.printf("작성자 : %s\n", nickname);
				System.out.printf("장소 : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("여행기간 : %s ~ %s\n", start_date, end_date);
				System.out.printf("작성 시간 : %s\n", w_time);
				System.out.printf("%s\n", text);
				// if(tnum == 현재로그인한 회원 번호) 비회원이면 0
				//		userState = 4;
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
			
			if(userState == 1)
				selectWork_ver_non();
			else if(userState == 2)
				selectWork_ver_trav();
			else if(userState == 3)
				selectWork_ver_admin();
			else if(userState == 4)	// 작성자인지 
				selectWork_ver_writter();
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectWork_ver_non() {
		System.out.println("1. 이전 화면  2. 댓글 보기 ");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// 이전화면
			// 객체 새로 생성해서 할것인가? 아니면 돌아갈 길이 있나?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// 댓글보기
			//reply view
			break;
		}
	}
	
	public void selectWork_ver_trav() {
		System.out.println("1. 이전 화면  2. 댓글 작성 3. 댓글 보기 4. 평가  5. 신고 6. 북마크 등록/해제");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// 이전화면
			// 객체 새로 생성해서 할것인가? 아니면 돌아갈 길이 있나?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// 댓글 작성
			//reply insert
			break;
		case 3:	// 댓글보기
			//reply view
			break;
		case 4:	// 평가
			//rating insert
			break;
		case 5:	// 신고
			//report insert
			break;
		case 6:	// 북마크 등록/해제
			//bookmark insert
			break;
		}
	}
	
	public void selectWork_ver_admin() {
		System.out.println("1. 신고 목록  2. 삭제 3. 유지");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// 신고 목록
			// 신고목록view
			break;
		case 2:	// 삭제
			//post delete
			break;
		case 3:	// 유지
			System.out.println("게시물을 유지합니다.");
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		}
	}
	
	public void selectWork_ver_writter() {
		System.out.println("1. 이전 화면  2. 수정 3. 삭제 4. 댓글 작성  5. 댓글 보기 6. 북마크 등록/해제");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// 이전화면
			// 객체 새로 생성해서 할것인가? 아니면 돌아갈 길이 있나?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// 수정
			// post 수정 view
			break;
		case 3:	// 삭제
			//post delete
			break;
		case 4:	// 댓글 작성
			//reply insert
			break;
		case 5:	// 댓글 보기
			//reply view
			break;
		case 6:	// 북마크 등록/해제
			//bookmark insert
			break;
		}
	}
}