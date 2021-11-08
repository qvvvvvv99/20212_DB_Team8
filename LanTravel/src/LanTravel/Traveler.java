//traveler�� ���ϴ� ����� �ۼ��� �ż����� Ŭ����
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
	
	//��������
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
			System.out.println("|                 ȸ����������                |");
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
			System.out.println("1. ��й�ȣ ����  2. �г��� ����  3. �̸��� ����  4. ���� ȭ��");
			System.out.println("�� ���� �����Ͻÿ�.");
			num = sc.nextInt();
			sc.nextLine();
			
			switch(num) {
			case 1: //��й�ȣ ����
				System.out.println("���ο� ��й�ȣ�� �Է��ϼ��� : ");
				input = sc.nextLine();
				
				sql = "update traveler set pw = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				break;
				
			case 2: //�г��� ����
				while(true) {
					System.out.println("���ο� �г����� �Է��ϼ��� : ");
					input = sc.nextLine();
					sql = "Select nickname from traveler where nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, input);
					rs = ps.executeQuery();
					
					if (rs.next()) {//�ߺ� ���̵� �����ϸ� ���� ����
						System.out.println("�г����� �����մϴ�.");
					}
						else break;
					}
				
				sql = "update traveler set nickname = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Tnum);
				rs = ps.executeQuery();
				
				break;
				
			case 3: //�̸��� ����
				System.out.println("���ο� �̸����� �Է��ϼ��� : ");
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
				System.out.println("�߸� �Է��Ͽ����ϴ�.");
				break;
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�ϸ�ũ ����Ʈ ���
	public void list_bookmark(Connection conn, Statement stmt) {
		Console console = new Console(); //console�� printPost �ż��带 ����ϱ� ����. (db�� ���� �����ϱ� ������ ��� ���� �ż���� �ٸ� Ŭ������ ����� ���� ���?) 
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
		System.out.println("|                  �ϸ�ũ ���                 |");
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
			
			//�ӽ� �ϸ�ũ ���(console�� printPostSelection �ż��带 �̿��ϴ� ������...)
			System.out.println("1. �󼼺���  2. ���� ������");
			System.out.println("�� ���� �Է��ϼ���.");
			num = sc.nextInt();
			
			switch(num) {
			case 1:
				System.out.println("����Ʈ ���� ���");
				System.out.println("����Ʈ ��ȣ�� �Է��ϼ���");
				input = sc.nextInt();
				
				sql = "select * from post_view p, (select bookmark from traveler_bookmarks where traveler_num = ?) b where b.bookmark = p.post_num";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Tnum);
				rs = ps.executeQuery();
				
				while (rs.next()) {
					if(input == rs.getInt(2)) isexist = true;
				}
				
				if(isexist) console.printPost(input);
				else System.out.println("�߸��� ��ȣ�Դϴ�.");
				
				break;
			case 2:
				break;
			}
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//�ϸ�ũ ���/����
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
				System.out.println("�ϸ�ũ�� �����Ǿ����ϴ�.");
			}
			else {
				sql = "insert into traveler_bookmarks values(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Tnum);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("�ϸ�ũ�� ��ϵǾ����ϴ�.");
			}
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//����Ʈ �ۼ�
	public void write_post(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		System.out.println("--------------------------------------------");
		System.out.println("|                 ����Ʈ �ۼ�                 |");
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
				System.out.println("���: " + country + " " + city + " " + name);
				System.out.println("���� �Ⱓ: " + start_date + " ~ " + end_date);
				System.out.println("����: " + text);
				System.out.println("����: " + picture);
				System.out.println("�±�: " + tag);
				
				System.out.println("�ۼ��� �׸��� �����ϼ���.");
				System.out.println("1.���  2.���� �Ⱓ  3.����  4.����  5.�±�  6.���  7.����");
				num = sc.nextInt();
				sc.nextLine();
				
				switch(num) {
				case 1:
					System.out.println("���� ��Ҹ� �Է��ϼ���.");
					System.out.print("1. ����: ");
					country = sc.nextLine();
					System.out.print("2. ����: ");
					city = sc.nextLine();
					System.out.print("3. �̸�: ");
					name = sc.nextLine();
					break;
				case 2:
					System.out.println("���� �Ⱓ�� �Է��ϼ���.(yyyy-mm-dd)");
					System.out.print("1. ������: ");
					start_date = sc.nextLine();
					System.out.print("2. ������: ");
					end_date = sc.nextLine();
					break;
				case 3:
					System.out.println("������ �Է��ϼ���.");
					text = sc.nextLine();
					break;
				case 4:
					System.out.println("���� ��θ� �Է��ϼ���.(����� ����)");
					picture = sc.nextLine();
					break;
				case 5:
					System.out.println("�ؽ��±׸� �Է��ϼ���.(����� ����)");
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