package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostPage {
	public static int userState;	// ����� ����(1:��ȸ�� 2:TRAVELER 3:ADMIN 4:�ۼ���)
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
	
	// 3. ��ȭ�� (����Ʈ)
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
				int tnum = rs.getInt(9);	// �ۼ������� Ȯ�� �뵵
				System.out.printf("�ۼ��� : %s\n", nickname);
				System.out.printf("��� : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("����Ⱓ : %s ~ %s\n", start_date, end_date);
				System.out.printf("�ۼ� �ð� : %s\n", w_time);
				System.out.printf("%s\n", text);
				// if(tnum == ����α����� ȸ�� ��ȣ) ��ȸ���̸� 0
				//		userState = 4;
			}
			
			sql = "SELECT h.tag_name " + 
					"from post p, hashtag h " + 
					"where h.post_num = p.post_num " + 
					"and p.Post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pNum);
			rs = ps.executeQuery();
			
			System.out.print("�±� : ");
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
			System.out.println("���� : " + score);
			
			if(userState == 1)
				selectWork_ver_non();
			else if(userState == 2)
				selectWork_ver_trav();
			else if(userState == 3)
				selectWork_ver_admin();
			else if(userState == 4)	// �ۼ������� 
				selectWork_ver_writter();
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectWork_ver_non() {
		System.out.println("1. ���� ȭ��  2. ��� ���� ");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// ����ȭ��
			// ��ü ���� �����ؼ� �Ұ��ΰ�? �ƴϸ� ���ư� ���� �ֳ�?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// ��ۺ���
			//reply view
			break;
		}
	}
	
	public void selectWork_ver_trav() {
		System.out.println("1. ���� ȭ��  2. ��� �ۼ� 3. ��� ���� 4. ��  5. �Ű� 6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// ����ȭ��
			// ��ü ���� �����ؼ� �Ұ��ΰ�? �ƴϸ� ���ư� ���� �ֳ�?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// ��� �ۼ�
			//reply insert
			break;
		case 3:	// ��ۺ���
			//reply view
			break;
		case 4:	// ��
			//rating insert
			break;
		case 5:	// �Ű�
			//report insert
			break;
		case 6:	// �ϸ�ũ ���/����
			//bookmark insert
			break;
		}
	}
	
	public void selectWork_ver_admin() {
		System.out.println("1. �Ű� ���  2. ���� 3. ����");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// �Ű� ���
			// �Ű���view
			break;
		case 2:	// ����
			//post delete
			break;
		case 3:	// ����
			System.out.println("�Խù��� �����մϴ�.");
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		}
	}
	
	public void selectWork_ver_writter() {
		System.out.println("1. ���� ȭ��  2. ���� 3. ���� 4. ��� �ۼ�  5. ��� ���� 6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");
		
		switch(num) {
		case 1:	// ����ȭ��
			// ��ü ���� �����ؼ� �Ұ��ΰ�? �ƴϸ� ���ư� ���� �ֳ�?
			mainpage mp = new mainpage();
			mp.mainpage(conn, stmt);
			break;
		case 2:	// ����
			// post ���� view
			break;
		case 3:	// ����
			//post delete
			break;
		case 4:	// ��� �ۼ�
			//reply insert
			break;
		case 5:	// ��� ����
			//reply view
			break;
		case 6:	// �ϸ�ũ ���/����
			//bookmark insert
			break;
		}
	}
}