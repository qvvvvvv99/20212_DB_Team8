//traveler가 지니는 기능을 작성한 매서드의 클래스
package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Traveler {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;
	
	private int Tnum;
	
	public Traveler() {
		this.conn = null;
		this.stmt = null;
		Tnum = -1;
	}
	
	public void setNum(int Tnum) {
		this.Tnum = Tnum;
	}
	
	//정보수정
	public void update(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String id = "";
		String pw = "";
		String nickname = "";
		String email = "";
		String input = "";
		
		int num;
		
		try {
			System.out.println("--------------------------------------------");
			System.out.println("|                 회원정보수정                |");
			System.out.println("--------------------------------------------");
			
			sql = "Select * from traveler where num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
	
			if (rs.next()) {
				id = rs.getString(2);
				pw = rs.getString(3);
				nickname = rs.getString(4);
				email = rs.getString(5);
			}
			
			System.out.println("ID       : " + id);
			System.out.println("PW       : " + pw);
			System.out.println("nickname : " + nickname);
			System.out.println("email    : " + email);
			System.out.println("--------------------------------------------");
			System.out.println("1. 비밀번호 수정  2. 닉네임 수정  3. 이메일 수정  4. 이전 화면");
			System.out.println("할 일을 선택하시오.");
			num = sc.nextInt();
			sc.nextLine();
			
			switch(num) {
			case 1: //비밀번호 수정
				System.out.println("새로운 비밀번호를 입력하세요 : ");
				input = sc.nextLine();
				
				sql = "update traveler set pw = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				break;
				
			case 2: //닉네임 수정
				while(true) {
					System.out.println("새로운 닉네임을 입력하세요 : ");
					input = sc.nextLine();
					sql = "Select nickname from traveler where nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, input);
					rs = ps.executeQuery();
					
					if (rs.next()) {//중복 아이디가 존재하면 새로 받음
						System.out.println("닉네임이 존재합니다.");
					}
						else break;
					}
				
				sql = "update traveler set nickname = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				
				break;
				
			case 3: //이메일 수정
				System.out.println("새로운 이메일을 입력하세요 : ");
				input = sc.nextLine();
				
				sql = "update traveler set email = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				break;
			
			case 4:
				break;
				
			default:
				System.out.println("잘못 입력하였습니다.");
				break;
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//북마크 리스트 출력
	public void list_bookmark(Connection conn, Statement stmt) {
		Console console = new Console(); //console의 printPost 매서드를 사용하기 위함. (db에 새로 접속하기 때문에 출력 관련 매서드는 다른 클래스를 만드는 것이 어떨지?) 
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		int post_num = 0;
		String name = "";
		String city = "";
		String written_time = "";
		
		int num;
		int input;
		boolean isexist = false;
		
		System.out.println("--------------------------------------------");
		System.out.println("|                  북마크 목록                 |");
		System.out.println("--------------------------------------------");
		
		try {
			sql = "select * from post_view p, (select bookmark from traveler_bookmarks where traveler_num = ?) b where b.bookmark = p.post_num";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
			
			System.out.println(
					" num  |                   name                   |         city          |         time        ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			
			while (rs.next()) {
				post_num = rs.getInt(2);
				name = rs.getString(3);
				city = rs.getString(4);
				written_time = rs.getString(5);
				
				System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", post_num, name, city, written_time);
			}
			
			//임시 북마크 기능(console의 printPostSelection 매서드를 이용하는 쪽으로...)
			System.out.println("1. 상세보기  2. 이전 페이지");
			System.out.println("할 일을 입력하세요.");
			num = sc.nextInt();
			
			switch(num) {
			case 1:
				System.out.println("포스트 선택 모드");
				System.out.println("포스트 번호를 입력하세요");
				input = sc.nextInt();
				
				sql = "select * from post_view p, (select bookmark from traveler_bookmarks where traveler_num = ?) b where b.bookmark = p.post_num";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Tnum);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					if(input == rs.getInt(2)) isexist = true;
				}
				
				if(isexist) console.printPost(input);
				else System.out.println("잘못된 번호입니다.");
				
				break;
			case 2:
				break;
			}
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//북마크 등록/해제
	public void enroll_bookmark(Connection conn, Statement stmt, int pnum) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		try {
			sql = "select * from traveler_bookmarks where traveler_num = ? and bookmark = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			ps.setInt(2, pnum);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				sql = "delete from traveler_bookmarks where traveler_num = ? and bookmark = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Tnum);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("북마크가 해제되었습니다.");
			}
			else {
				sql = "insert into traveler_bookmarks values(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Tnum);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("북마크가 등록되었습니다.");
			}
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//포스트 작성
	public void write_post(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		System.out.println("--------------------------------------------");
		System.out.println("|                 포스트 작성                 |");
		System.out.println("--------------------------------------------");
		
		int num = 0;
		int pnum = 0;
		String start_date = "0000-00-00";
		String end_date = "0000-00-00";
		String text = "";
		String name = "";
		String country = "";
		String city = "";
		String picture = "";
		String tag = "";
		
		try {
			sql = "select count(*) from post";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				pnum = rs.getInt(1);
			}
			
			while(true) {
				System.out.println("장소: " + country + " " + city + " " + name);
				System.out.println("여행 기간: " + start_date + " ~ " + end_date);
				System.out.println("본문: " + text);
				System.out.println("사진: " + picture);
				System.out.println("태그: " + tag);
				
				System.out.println("작성할 항목을 선택하세요.");
				System.out.println("1.장소  2.여행 기간  3.본문  4.사진  5.태그  6.등록  7.종료");
				num = sc.nextInt();
				sc.nextLine();
				
				switch(num) {
				case 1:
					System.out.println("여행 장소를 입력하세요.");
					System.out.print("1. 국가: ");
					country = sc.nextLine();
					System.out.print("2. 도시: ");
					city = sc.nextLine();
					System.out.print("3. 이름: ");
					name = sc.nextLine();
					break;
				case 2:
					System.out.println("여행 기간을 입력하세요.(yyyy-mm-dd)");
					System.out.print("1. 시작일: ");
					start_date = sc.nextLine();
					System.out.print("2. 종료일: ");
					end_date = sc.nextLine();
					break;
				case 3:
					System.out.println("본문을 입력하세요.");
					text = sc.nextLine();
					break;
				case 4:
					System.out.println("사진 경로를 입력하세요.(띄어쓰기로 구분)");
					picture = sc.nextLine();
					break;
				case 5:
					System.out.println("해시태그를 입력하세요.(띄어쓰기로 구분)");
					break;
				case 6:
					break;
				case 7:
					break;
				}
			}
			
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}