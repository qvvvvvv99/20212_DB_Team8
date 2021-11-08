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
	private int Travel_num;
	
	public Guest() {
		this.conn = null;
		this.stmt = null;
		id = null;
		pw = null;
		Travel_num = -1;
	}
	
	public void join(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String join_id = "";
		String join_pw = "";
		String nickname = "";
		String email = "";
		int Tnum = -1;
		
		try {
			/****아이디****/
			while(true) {
			System.out.print("아이디를 입력하세요.");
				join_id = sc.nextLine();
				sql = "Select id from traveler where id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, join_id);
				rs = ps.executeQuery();
			
				if (rs.next()) {//중복 아이디가 존재하면 새로 받음
					System.out.println("아이디가 존재합니다.");
				}
				else break;
			}
			
			/****비밀번호****/
			System.out.print("비밀번호를 입력하세요.");
			join_pw = sc.nextLine();
			
			/****닉네임****/
			while(true) {
				System.out.print("닉네임을 입력하세요.");
					nickname = sc.nextLine();
					sql = "Select nickname from traveler where nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, nickname);
					rs = ps.executeQuery();
				
					if (rs.next()) {//중복 닉네임이 존재하면 새로 받음
						System.out.println("닉네임이 존재합니다.");
					}
					else break;
				}
			
			/****이메일****/
			System.out.print("이메일을 입력하세요.");
			email = sc.nextLine();
			
			/****tuple 개수 count****/
			sql = "Select count(*) from traveler";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		
			if (rs.next()) {
				Tnum = rs.getInt(1) + 1;
			}
			
			/****input****/
			sql = "insert into traveler values(?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			ps.setString(2, join_id);
			ps.setString(3, join_pw);
			ps.setString(4, nickname);
			ps.setString(5, email);
			rs = ps.executeQuery();

			System.out.println("회원가입이 완료되었습니다.");
			System.out.println("--------------------------------");
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean login(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		System.out.print("아이디를 입력하세요: ");
		id = sc.nextLine();
		System.out.print("비밀번호를 입력하세요: ");
		pw = sc.nextLine();
		
		try {
			sql = "Select num from traveler where id = ? and pw = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();
	
			if (rs.next()) {
				Travel_num = rs.getInt(1);
				System.out.println("로그인에 성공하였습니다.");
				return true;
			}
			else {
				System.out.println("로그인에 실패하였습니다.");
				id = null;
				pw = null;
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPw() {
		return pw;
	}
	
	public int getTnum() {
		return Travel_num;
	}
}