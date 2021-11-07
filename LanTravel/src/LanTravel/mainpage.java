package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class mainpage {
	public static final int postsPerPage = 10;
	public static int page = 1;
	public static int userState = 1;	// ����� ����(1:��ȸ�� 2:TRAVELER 3:ADMIN)
	// changePage �Լ����� ��밡��
	Connection conn;
	Statement stmt;
	int num;
	int count;	// ������ ������ Ȯ���ϱ� ���ؼ� page���� rs.next()�� Ƚ�� ī��Ʈ -> 10���� ������ �������� / 10�̿��� ������ �� �� �ִ�(��� ����)
	Scanner sc = new Scanner(System.in);
	String search;	// �˻� �� �ܾ� �Է� �޴� ���ڿ�
	int[] postNum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};	// ���� �Ҷ� ���� pNum ������ ���ڿ�
	
	// post ������ ȸ��/��ȸ�� ���о��� �� �� �ִ�. ADMIN���� �α��ν� �Ⱥ���
	public void mainpage(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		count = 0;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * from post_view where no between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, (page - 1) * postsPerPage + 1);
			ps.setInt(2, page * postsPerPage);
			rs = ps.executeQuery();

			System.out.println(" num  |           name           |      city      |         time        ");
			System.out.println("------------------------------------------------------------------------");
			while (rs.next()) {
				int Pnum = rs.getInt(2);
				String name = rs.getString(3);
				String city = rs.getString(4);
				String time = rs.getString(5);
				System.out.printf("%5d | %20s| %10s| %20s\n", Pnum, name, city, time);
				postNum[count] = Pnum;
				count++;
			}
			if(userState == 1)
				selectWork_ver_non();
			else if(userState == 2)
				selectWork_ver_mem();

			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ��ȸ�� �� ���θ޴� ���ú�
	public void selectWork_ver_non() {
		System.out.println("1. ����  2. �α���  3. ����  4. ����  5. ����  6. �˻�  7. ����");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");

		switch (num) {
		case 1:
			break;
		case 2:	// �α��� ������ �� userState 2(TRAVELER) or 3(ADMIN)���� �ٲٰ� mainpage(conn, stmt) ȣ��
			// test
			userState = 2;	// TRAVELER
			break;
		case 3:		// ����
			// ù������ �϶�
			if(page == 1) {
				System.out.println("ù ���Դϴ�.");
				selectWork_ver_non();
			}
			else {
				page--;
				mainpage(conn, stmt);
			}
			break;
		case 4:		// ����
			// --------------������������ �϶� Ȯ���ϴ� ��
			if(count < 10) {
				System.out.println("���������Դϴ�.");
				selectWork_ver_non();
			}
			else {
				page++;
				mainpage(conn, stmt);
			}
			break;
		case 5:		// ���� - ����ȭ������ �ٲܷ��� rs �ٽ� �����÷��� try catch ���� �������� �� ���Ƽ� �ϴ� ������ ���� 0~9 ���̶� �����ϰ� �Է¹��� ������ ����Ʈ ����(���� ȭ�� �� ����)
			System.out.print("���° ����Ʈ�� �����Ͻðڽ��ϱ�?");
			num = sc.nextInt();
			System.out.printf("\n\n");
			PostPage pp = new PostPage(conn, stmt, userState, postNum[num]);
			break;
		case 6:
			break;
		case 7:
			System.out.println("�����մϴ�.");
			break;
		}
	}
	
	// TRAVELER �� ���θ޴� ���ú�
	public void selectWork_ver_mem() {
		System.out.println("1. ȸ�� ���� ����  2. �α׾ƿ�  3. ����  4. ����  5. ����  6. �˻�  7. ����Ʈ ���� 8. �ϸ�ũ 9. ���� ");
		System.out.print("�� ���� �����Ͻÿ�.");
		num = sc.nextInt();
		System.out.printf("\n\n");

		switch (num) {
		case 1:
			break;
		case 2:	// �α׾ƿ� -> userState = 1
			userState = 1;
			mainpage(conn, stmt);
			break;
		case 3:		// ����
			// ù������ �϶�
			if(page == 1) {
				System.out.println("ù ���Դϴ�.");
				selectWork_ver_non();
			}
			else {
				page--;
				mainpage(conn, stmt);
			}
			break;
		case 4:		// ����
			// --------------������������ �϶� Ȯ���ϴ� ��
			if(count < 10) {
				System.out.println("���������Դϴ�.");
				selectWork_ver_non();
			}
			else {
				page++;
				mainpage(conn, stmt);
			}
			break;
		case 5:		// ����
			System.out.print("���° ����Ʈ�� �����Ͻðڽ��ϱ�?");
			num = sc.nextInt();
			System.out.printf("\n\n");
			PostPage pp = new PostPage(conn, stmt, userState, postNum[num]);
			break;
		case 6:		// �˻�
			System.out.print("�˻� ������ �Է��Ͻÿ�");
			search = sc.next();
			break;
		case 7:		// ����Ʈ ����
			break;
		case 8:		// �ϸ�ũ
			break;
		case 9:		// ����
			System.out.println("�����մϴ�.");
			break;
		}
	}
}