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
			/****���̵�****/
			while(true) {
			System.out.print("���̵� �Է��ϼ���.");
				join_id = sc.nextLine();
				sql = "Select id from traveler where id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, join_id);
				rs = ps.executeQuery();
			
				if (rs.next()) {//�ߺ� ���̵� �����ϸ� ���� ����
					System.out.println("���̵� �����մϴ�.");
				}
				else break;
			}
			
			/****��й�ȣ****/
			System.out.print("��й�ȣ�� �Է��ϼ���.");
			join_pw = sc.nextLine();
			
			/****�г���****/
			while(true) {
				System.out.print("�г����� �Է��ϼ���.");
					nickname = sc.nextLine();
					sql = "Select nickname from traveler where nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, nickname);
					rs = ps.executeQuery();
				
					if (rs.next()) {//�ߺ� �г����� �����ϸ� ���� ����
						System.out.println("�г����� �����մϴ�.");
					}
					else break;
				}
			
			/****�̸���****/
			System.out.print("�̸����� �Է��ϼ���.");
			email = sc.nextLine();
			
			/****tuple ���� count****/
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

			System.out.println("ȸ�������� �Ϸ�Ǿ����ϴ�.");
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
		
		System.out.print("���̵� �Է��ϼ���: ");
		id = sc.nextLine();
		System.out.print("��й�ȣ�� �Է��ϼ���: ");
		pw = sc.nextLine();
		
		try {
			sql = "Select num from traveler where id = ? and pw = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, pw);
			rs = ps.executeQuery();
	
			if (rs.next()) {
				Travel_num = rs.getInt(1);
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
				return true;
			}
			else {
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
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