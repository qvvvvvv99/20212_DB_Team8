//traveler가 지니는 기능을 작성한 매서드의 클래스
package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				System.out.println("새로운 비밀번호를 입력하세요(취소: 0) : ");
				input = sc.nextLine();
				if(input.equals("0")) {
					System.out.println("취소되었습니다.");
					update(conn, stmt);
					return;
				}
				
				sql = "update traveler set pw = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				
				System.out.println("비밀번호가 수정되었습니다.");
				break;
				
			case 2: //닉네임 수정
				while(true) {
					System.out.println("새로운 닉네임을 입력하세요 (취소: 0) : ");
					input = sc.nextLine();
					
					if(input.equals("0")) {
						System.out.println("취소되었습니다.");
						update(conn, stmt);
						return;
					}
					
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
				
				System.out.println("닉네임이 수정되었습니다.");
				
				break;
				
			case 3: //이메일 수정
				System.out.println("새로운 이메일을 입력하세요(취소: 0) : ");
				input = sc.nextLine();
				
				if(input.equals("0")) {
					System.out.println("취소되었습니다.");
					update(conn, stmt);
					return;
				}
				
				sql = "update traveler set email = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				
				System.out.println("이메일이 수정되었습니다.");
				
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
		String start_date = null;
		String end_date = null;
		String text = null;
		String name = null;
		String country = null;
		String city = null;
		String picture = null;
		String tag = null;
		
		int state = 0; //0.작성 1.등록 2.종료
		
		try {
			sql = "select max(post_num) from post";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				pnum = rs.getInt(1) + 1;
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
					tag = sc.nextLine();
					break;
				case 6:
					state = 1;
					break;
				case 7:
					state = 2;
					break;
				}
				
				if(state == 1) { //insert
					//TODO null 여부 확인 후 insert
					if(name==null) {
						System.out.println("여행 장소가 비어 있습니다.");
						state = 0;
					}
					else {
						//아래 식에서 sysdate를 받아오지 못함. 
						//2021-11-09 2시 16분에 작성하였다면, 0021-11-09 00:00:00 으로 insert
						sql = "insert into post values(?, to_date(?,'yyyy-mm-dd'), to_date(?, 'yyyy-mm-dd'), ?, to_date(sysdate, 'yyyy-mm-dd hh24:mi:ss'), ?)";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, pnum);
						ps.setString(2, start_date);
						ps.setString(3, end_date);
						ps.setString(4, text);
						ps.setInt(5, Tnum);
						rs = ps.executeQuery();
						
						sql = "insert into post_locations values(?, ?, ?, ?)";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, pnum);
						ps.setString(2, name);
						ps.setString(3, country);
						ps.setString(4, city);
						rs = ps.executeQuery();
						
						if(picture != null) { //사진 등록
							String []pictokens = picture.split(" ");
							int picnum = 0;
							
							for (int i=0; i<pictokens.length; i++) {
								sql = "select count(*) from post_pictures";
								ps = conn.prepareStatement(sql);
								rs = ps.executeQuery();
								
								while (rs.next()) {
									picnum = rs.getInt(1) + 1;
								}
								
								sql = "insert into post_pictures values(?, ?, ?)";
								ps = conn.prepareStatement(sql);
								ps.setInt(1, pnum);
								ps.setInt(2, picnum);
								ps.setString(3, pictokens[i]);
								rs = ps.executeQuery();
							}
						}
						
						if(tag != null) { //태그 등록
							String []tagtokens = tag.split(" #");
							int tagnum = 0;
							
							for (int i=0; i<tagtokens.length; i++) {
								sql = "select count(*) from hashtag";
								ps = conn.prepareStatement(sql);
								rs = ps.executeQuery();
								
								while (rs.next()) {
									tagnum = rs.getInt(1) + 1;
								}
								
								sql = "insert into hashtag values(?, ?, ?)";
								ps = conn.prepareStatement(sql);
								ps.setInt(1, pnum);
								ps.setInt(2, tagnum);
								ps.setString(3, tagtokens[i]);
								rs = ps.executeQuery();
							}
						}
						
						System.out.println("등록되었습니다.");
						break;
					}
				}
				
				if(state == 2) { //end
					System.out.println("종료합니다.");
					break;
				}
			}		
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//신고
	public void report(Connection conn, Statement stmt, int pnum, String type) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String reason = null;
		int rep_num = 0;
		
		System.out.println("포스트 신고");
		
		try {
			sql = "select count(*) from report";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}
			
			while(reason == null) {
				System.out.println("신고 사유를 입력하세요(취소: 0)");
				reason = sc.nextLine();
				if(reason.equals("0")) {
					System.out.println("취소되었습니다.");
					return;
				}
				if(reason == null) System.out.println("신고 사유가 공란입니다.");
			}
			
			sql = "insert into report values(?, ?, ?, ?, 1)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rep_num);
			ps.setString(2, type);
			ps.setString(3, reason);
			ps.setInt(4, pnum);
			rs = ps.executeQuery();
			//포스트 신고
			if(type == "P") {
				sql = "insert into record values(?, 0, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ps.setInt(2, rep_num);
				rs = ps.executeQuery();
			}
			//댓글 신고(미완료)
			else if(type == "r") {
				sql = "insert into record values(0, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ps.setInt(2, rep_num);
				rs = ps.executeQuery();
			}
			
			System.out.println("신고가 완료되었습니다.");
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//댓글 작성(대댓x)
	public void reply_to_post(Connection conn, Statement stmt, int pnum) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String reply = null;
		int rep_num = 0;
		
		System.out.println("댓글 작성 (취소: 0)");
		
		try {
			sql = "select max(reply_num) from reply";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}
			
			while(reply == null) {
				System.out.println("댓글을 입력하세요");
				reply = sc.nextLine();
				if(reply.equals("0")) {
					break;
				}
				if(reply == null) System.out.println("댓글이 공란입니다.");
			}
			if(reply.equals("0")) {
				System.out.println("취소되었습니다.");
			}
			else {
				sql = "insert into reply values(?, ?, to_date(sysdate,'yyyy-mm-dd hh24:mi:ss'), ?, null, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rep_num);
				ps.setString(2, reply);
				ps.setInt(3, Tnum);
				ps.setInt(4, pnum);
				rs = ps.executeQuery();
			
				System.out.println("댓글을 작성하였습니다.");
			}
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//평가
	public void rating(Connection conn, Statement stmt, int pnum) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		int rate = 0;
		boolean isuploaded = false;
	
		System.out.println("별점 작성");
		
		try {
			sql = "select * from rating where post_num = ? and traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setInt(2, Tnum);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				System.out.println("이미 별점을 작성하였습니다.");
			}
			else {
				while(!isuploaded) {
					System.out.println("별점을 입력하세요.(1, 2, 3, 4, 5), (취소: 0)");
					rate = sc.nextInt();
					sc.nextLine();
					
					switch(rate) {
					case 0:
						System.out.println("취소되었습니다.");
						return;
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						sql = "insert into rating values(?, ?, ?)";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, pnum);
						ps.setInt(2, Tnum);
						ps.setInt(3, rate);
						rs = ps.executeQuery();
						System.out.println("별점이 반영되었습니다.");
						isuploaded = true;
						break;
					default:
						System.out.println("잘못된 값입니다.");
						break;
					}
				}
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getTnum() {
		return Tnum;
	}
}