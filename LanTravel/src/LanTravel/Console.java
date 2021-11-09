package LanTravel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Console {
	public static final int postsPerPage = 10;
	Database db = null;
	Connection conn = null;
	Statement stmt = null;
	String search;

	Guest guest;
	Traveler traveler;
	Admin admin;

	Scanner sc = new Scanner(System.in);
	int user; // 1: Guest, 2: Traveler, 3: Admin
	int page;
	int mode; // 1: �Ϲ�, 2: ����
	boolean isWriter;

	public Console() {
		db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();

		user = 1;
		page = 1;
		mode = 1;
		isWriter = false;
	}

	// Post Table ǥ��
	public void printPostTable() {
		int totalPost = 0; // �� Post ��
		int lastPage = 0; // ������ Page

		// totalPost, lastPage ���
		try {
			String sql = "SELECT COUNT(post_num) FROM post";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				totalPost = rs.getInt(1) - 1; // -1 : 0�� post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lastPage = totalPost / postsPerPage;
		lastPage = (totalPost % postsPerPage == 0) ? lastPage : lastPage + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (page < 1) {
				page = 1;
				System.out.println("page: " + page + " / " + lastPage + "\t[ù �������Դϴ�.]");
			} else if (page > lastPage) {
				page = lastPage;
				System.out.println("page: " + page + " / " + lastPage + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + page + " / " + lastPage);
			}
		}

		// Post Table ǥ��
		try {
			String sql = "SELECT * FROM post_view WHERE no BETWEEN ? AND ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, (page - 1) * postsPerPage + 1);
			ps.setInt(2, page * postsPerPage);
			ResultSet rs = ps.executeQuery();

			System.out.println(
					" num  |                   name                   |         city          |         time        ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			while (rs.next()) {
				int no = rs.getInt(1);
				int pnum = rs.getInt(2);
				String name = rs.getString(3);
				String city = rs.getString(4);
				String time = rs.getString(5);
				if (mode == 2) { // ���� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", no - ((page - 1) * postsPerPage), name, city,
							time);
				} else { // �Ϲ� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printMainMenu() {
		// TODO: ClearConsole ����

		// Post Table ǥ��
		printPostTable();

		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ����Ʈ�� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printMainMenu();
			} else { // Post ����
				try {
					// pnum ����
					String sql = "SELECT post_num FROM post_view WHERE no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, no + (page - 1) * postsPerPage);
					ResultSet rs = ps.executeQuery();

					int pnum = 0;
					if (rs.next()) {
						pnum = rs.getInt(1);
					}
					mode = 1;

					if (pnum == 0) { // ����
						System.out.println("�߸� ���õǾ����ϴ�.");
						printMainMenu();
					} else { // Post �� ǥ��
						printPost(pnum);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // �Ϲ� mode
			// User�� ���� �޴� ǥ��
			switch (user) {
			case 1:
				printMainMenu_guest();
				break;
			case 2:
				printMainMenu_traveler();
				break;
			case 3:
//				printMainMenu_Admin();
				break;
			}
		}
	}

	// Guest ���� �޴�
	public void printMainMenu_guest() {
		guest = new Guest();

		System.out.println("1. ����  2. �α���  3. ����  4. ����  5. ����  6. �˻�  7. ����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			guest.join(conn, stmt);
			printMainMenu();
			break;
		case 2: // �α��� ������ �� user 2(TRAVELER) or 3(ADMIN)���� �ٲٰ� printMainMenu() ȣ��
			boolean isLogined;
			int Tnum;
			int Anum;
			
			isLogined = guest.login(conn, stmt);
			if (isLogined) {
				user = guest.getType() + 1; 
				switch(user) {
				case 2: // TRAVELER
					Tnum = guest.getTnum();
					traveler = new Traveler();
					traveler.setNum(Tnum);
					break;
				case 3:
					Anum = guest.getAnum();
					admin = new Admin();
					admin.setNum(Anum);
					break;
				}
			}
			printMainMenu();
			break;
		case 3: // ����
			page--;
			printMainMenu();
			break;
		case 4: // ����
			page++;
			printMainMenu();
			break;
		case 5: // ����
			mode = 2;
			printMainMenu();
			break;
		case 6:	// �˻�
			System.out.print("�˻� ������ �Է��ϼ���. ");
			search = sc.next();
			searchPost(search);
			break;
		case 7:
			System.out.println("�����մϴ�.");
			break;
		}
	}

	// Traveler ���� �޴�
	public void printMainMenu_traveler() {
		System.out.println("1. ȸ�� ���� ����  2. �α׾ƿ�  3. ����  4. ����  5. ����  6. �˻�  7. ����Ʈ ����  8. �ϸ�ũ  9. ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			traveler.update(conn, stmt);
			printMainMenu();
			break;
		case 2: // �α׾ƿ� -> user = 1
			user = 1;
			printMainMenu();
			break;
		case 3: // ����
			page--;
			printMainMenu();
			break;
		case 4: // ����
			page++;
			printMainMenu();
			break;
		case 5: // ����
			mode = 2;
			printMainMenu();
			break;
		case 6: // �˻�
			System.out.print("�˻� ������ �Է��ϼ���. ");
			search = sc.next();
			break;
		case 7: // ����Ʈ ����
			traveler.write_post(conn, stmt);
			printMainMenu();
			break;
		case 8: // �ϸ�ũ
			traveler.list_bookmark(conn, stmt);
			printMainMenu();
			break;
		case 9: // ����
			System.out.println("�����մϴ�.");
			break;
		}
	}

	// Post �� ǥ��
	public void printPost(int pnum) {
		ResultSet rs = null;

		try {
			String sql = "SELECT p.Start_date, p.End_date, p.Text, p.Written_time, pl.Name, pl.Country, pl.City, t.Nickname, t.num  "
					+ "from post p, traveler t, post_locations pl " + "where p.traveler_num = t.num "
					+ "and pl.post_num = p.post_num " + "and p.Post_num = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
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
				int tnum = rs.getInt(9); // �ۼ������� Ȯ�� �뵵
				System.out.printf("�ۼ��� : %s\n", nickname);
				System.out.printf("��� : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("����Ⱓ : %s ~ %s\n", start_date, end_date);
				System.out.printf("�ۼ� �ð� : %s\n", w_time);
				System.out.printf("%s\n", text);
				// if(tnum == ����α����� ȸ�� ��ȣ) ��ȸ���̸� 0
				// isWriter = True;
			}

			sql = "SELECT h.tag_name " + "from post p, hashtag h " + "where h.post_num = p.post_num "
					+ "and p.Post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();

			System.out.print("�±� : ");
			while (rs.next()) {
				String tag = rs.getString(1);
				System.out.print("#" + tag + " ");
			}
			System.out.println();

			sql = "SELECT avg(r.score) " + "from post p, rating r " + "where r.post_num = p.post_num "
					+ "and p.post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();

			rs.next();
			int score = rs.getInt(1);
			System.out.println("���� : " + score);

			if (user == 2) { // Traveler
				if (isWriter) { // �ۼ�������
					printPostSelection_traveler_writer(pnum);
				} else { // ���ۼ���
					printPostSelection_traveler(pnum);
				}
			} else if (user == 3) { // Admin
				printPostSelection_admin();
			} else { // Guest (user == 1)
				printPostSelection_guest();
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printPostSelection_guest() {
		System.out.println("1. ���� ȭ��  2. ��� ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����ȭ��
			printMainMenu();
			break;
		case 2: // ��ۺ���
			// reply view
			break;
		}
	}

	public void printPostSelection_traveler(int pnum) {
		System.out.println("1. ���� ȭ��  2. ��� �ۼ�  3. ��� ����  4. ��  5. �Ű�  6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����ȭ��
			printMainMenu();
			break;
		case 2: // ��� �ۼ�
			// reply insert
			break;
		case 3: // ��ۺ���
			// reply view
			break;
		case 4: // ��
			// rating insert
			break;
		case 5: // �Ű�
			// report insert
			traveler.report(conn, stmt, pnum, "P");
			printPostSelection_traveler(pnum);
			break;
		case 6: // �ϸ�ũ ���/����
			traveler.enroll_bookmark(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	public void printPostSelection_traveler_writer(int pnum) {
		System.out.println("1. ���� ȭ��  2. ����  3. ����  4. ��� �ۼ�  5. ��� ����  6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����ȭ��
			printMainMenu();
			break;
		case 2: // ����
			// post ���� view
			break;
		case 3: // ����
			// post delete
			break;
		case 4: // ��� �ۼ�
			// reply insert
			break;
		case 5: // ��� ����
			// reply view
			break;
		case 6: // �ϸ�ũ ���/����
			traveler.enroll_bookmark(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	public void printPostSelection_admin() {
		System.out.println("1. �Ű� ���  2. ����  3. ����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // �Ű� ���
			// �Ű���view
			break;
		case 2: // ����
			// post delete
			break;
		case 3: // ����
			System.out.println("�Խù��� �����մϴ�.");
			printMainMenu();
			break;
		}
	}
	
	// �˻��˻��˻�
	public void searchPost(String str) {
		ResultSet rs = null;

		
		// �˻����� ��� �����ϱ�
		try {
			String sql = "SELECT p.Start_date, p.End_date, p.Text, p.Written_time, pl.Name, pl.Country, pl.City, t.Nickname, t.num  "
					+ "from post p, traveler t, post_locations pl " + "where p.traveler_num = t.num "
					+ "and pl.post_num = p.post_num " + "and p.Post_num = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, str);
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
				int tnum = rs.getInt(9); // �ۼ������� Ȯ�� �뵵
				System.out.printf("�ۼ��� : %s\n", nickname);
				System.out.printf("��� : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("����Ⱓ : %s ~ %s\n", start_date, end_date);
				System.out.printf("�ۼ� �ð� : %s\n", w_time);
				System.out.printf("%s\n", text);
				// if(tnum == ����α����� ȸ�� ��ȣ) ��ȸ���̸� 0
				// isWriter = True;
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
