package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;
	private int Anum;
	
	public Admin(){
		conn = null;
		stmt = null;
		Anum = 0;
	}
	
	public void setNum(int Anum) {
		this.Anum = Anum;
	}
	
	public void update(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String id = "";
		String pw = "";
		String input = "";
		
		int num;
		
		try {
			System.out.println("--------------------------------------------");
			System.out.println("|                 관리자정보수정                |");
			System.out.println("--------------------------------------------");
			
			sql = "Select * from admin where num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Anum);
			rs = ps.executeQuery();
	
			if (rs.next()) {
				id = rs.getString(2);
				pw = rs.getString(3);
			}
			
			System.out.println("ID       : " + id);
			System.out.println("PW       : " + pw);
			System.out.println("--------------------------------------------");
			System.out.println("1. 비밀번호 수정  2. 이전 화면");
			System.out.println("할 일을 선택하시오.");
			num = sc.nextInt();
			sc.nextLine();
			
			switch(num) {
			case 1: //비밀번호 수정
				System.out.println("새로운 비밀번호를 입력하세요 : ");
				input = sc.nextLine();
				
				sql = "update admin set pw = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Anum);
				rs = ps.executeQuery();
				break;
			
			case 2:
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

	public void Delete_Post_reply(Connection conn, Statement stmt, String type, int num)  {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		PreparedStatement ps = null;
		String sql = null;
		try {
			switch(type) {
			case "P":
				
				sql = "delete from reply where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				sql = "delete from rating where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				sql = "delete from post_pictures where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				sql = "delete from post_locations where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				sql = "delete from hashtag where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				sql = "select report_num from record where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				int rnum = 0;
				
				while (rs.next()) {
					rnum = rs.getInt(1);
					
					sql = "delete from record where report_num = ?";
					ps = conn.prepareStatement(sql);			
					ps.setInt(1, rnum);
					rs1 = ps.executeQuery();
					
					sql = "delete from report where report_num = ?";
					ps = conn.prepareStatement(sql);			
					ps.setInt(1, rnum);
					rs2 = ps.executeQuery();
				}
				
				sql = "delete from post where post_num = ?";
				ps = conn.prepareStatement(sql);			
				ps.setInt(1, num);
				rs = ps.executeQuery();
				
				System.out.println("포스트가 삭제되었습니다.");
			case "R":
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
