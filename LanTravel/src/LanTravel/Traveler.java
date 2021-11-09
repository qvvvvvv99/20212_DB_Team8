//traveler�� ���ϴ� ����� �ۼ��� �ż����� Ŭ����
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
		String start_date = null;
		String end_date = null;
		String text = null;
		String name = null;
		String country = null;
		String city = null;
		String picture = null;
		String tag = null;
		
		int state = 0; //0.�ۼ� 1.��� 2.����
		
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
					//TODO null ���� Ȯ�� �� insert
					if(name==null) {
						System.out.println("���� ��Ұ� ��� �ֽ��ϴ�.");
						state = 0;
					}
					else {
						//�Ʒ� �Ŀ��� sysdate�� �޾ƿ��� ����. 
						//2021-11-09 2�� 16�п� �ۼ��Ͽ��ٸ�, 0021-11-09 00:00:00 ���� insert
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
						
						if(picture != null) { //���� ���
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
						
						if(tag != null) { //�±� ���
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
						
						System.out.println("��ϵǾ����ϴ�.");
						break;
					}
				}
				
				if(state == 2) { //end
					System.out.println("�����մϴ�.");
					break;
				}
			}		
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�Ű�
	public void report(Connection conn, Statement stmt, int pnum, String type) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String reason = null;
		int rep_num = 0;
		
		System.out.println("����Ʈ �Ű�");
		
		try {
			sql = "select count(*) from report";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}
			
			while(reason == null) {
				System.out.println("�Ű� ������ �Է��ϼ���");
				reason = sc.nextLine();
				if(reason == null) System.out.println("�Ű� ������ �����Դϴ�.");
			}
			
			sql = "insert into report values(?, ?, ?, ?, 1)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rep_num);
			ps.setString(2, type);
			ps.setString(3, reason);
			ps.setInt(4, pnum);
			rs = ps.executeQuery();
			//����Ʈ �Ű�
			if(type == "P") {
				sql = "insert into record values(?, 0, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ps.setInt(2, rep_num);
				rs = ps.executeQuery();
			}
			//��� �Ű�(�̿Ϸ�)
			else if(type == "r") {
				sql = "insert into record values(0, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ps.setInt(2, rep_num);
				rs = ps.executeQuery();
			}
			
			System.out.println("�Ű� �Ϸ�Ǿ����ϴ�.");
			
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}