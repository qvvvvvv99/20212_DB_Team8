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
	public static final int replyPerPage = 10;
	Database db = null;
	Connection conn = null;
	Statement stmt = null;
	String searchStr;

	Guest guest;
	Traveler traveler;
	Admin admin;

	Scanner sc = new Scanner(System.in);
	int user; // 1: Guest, 2: Traveler, 3: Admin
	int page;
	int mode; // 1: �Ϲ�, 2: ����
	boolean isWriter;
	int searchPage = 1;	// �˻��� page ��ȣ
	int replyPage = 1;
	int route;	// post �󼼺��Ⱑ �˻����� ���õǾ� �Դ���(0) ���ο��� ���õǾ� �Դ���(1) �ϸ�ũ���� ���ô�� �Դ���(2) ���� -> post �󼼺��⿡�� �������� ���ư��� ���

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
		
		route = 1;

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
				printMainMenu_admin();
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
			searchStr = sc.next();
			printSearchPost();
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
			searchStr = sc.next();
			printSearchPost();
			break;
		case 7: // ����Ʈ ����
			traveler.write_post(conn, stmt);
			printMainMenu();
			break;
		case 8: // �ϸ�ũ
			printBookmarkMenu();
			printMainMenu();
			break;
		case 9: // ����
			System.out.println("�����մϴ�.");
			break;
		}
	}
	
	//admin ���θ޴�
	public void printMainMenu_admin() {
		System.out.println("1. ȸ�� ���� ����  2. �α׾ƿ�  3. �Ű�� ��� ��ȸ  4. ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			admin.update(conn, stmt);
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
				printPostSelection_guest(pnum);
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printPostSelection_guest(int pnum) {
		System.out.println("1. ���� ȭ��  2. ��� ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����ȭ��
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
			break;
		case 2: // ��ۺ���
			printReply(pnum);
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
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
			else if(route == 2)
				printBookmarkMenu();
			break;
		case 2: // ��� �ۼ�
			// reply insert
			traveler.reply_to_post(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		case 3: // ��ۺ���
			printReply(pnum);
			break;
		case 4: // ��
			traveler.rating(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
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
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
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
			printReply(pnum);
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
	
	// �˻�
	public void printSearchPost() {
		printSearchPostTable();
		route = 0;
		
		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ����Ʈ�� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printSearchPost();
			} else { // Post ����
				try {
					// pnum ����
					String sql = "SELECT post_num FROM ( "
							+ "select rownum no, np.* "
							+ "from ( SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
							+ "FROM post p, post_locations pl, hashtag h "
							+ "WHERE p.post_num = pl.post_num "
							+ "and h.post_num = p.post_num "
							+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
							+ "ORDER BY p.written_time DESC ) np ) WHERE no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, "%"+searchStr+"%");
					ps.setString(2, "%"+searchStr+"%");
					ps.setString(3, "%"+searchStr+"%");
					ps.setString(4, "%"+searchStr+"%");
					ps.setString(5, "%"+searchStr+"%");
					ps.setInt(6, no + (searchPage - 1) * postsPerPage);
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
		}
		else
			printSearchPostMenu();
	}
	
	public void printSearchPostTable() {
		System.out.println("[ " + searchStr + " ] �˻� ����Դϴ�.");
		
		int searchTotalPost = 0; // �� Post ��
		int searchlastPage = 0; // ������ Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage ���
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ "(SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "FROM post p, post_locations pl, hashtag h "
					+ "WHERE p.post_num = pl.post_num "
					+ "and h.post_num = p.post_num "
					+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
					+ "ORDER BY p.written_time DESC)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+searchStr+"%");
			ps.setString(2, "%"+searchStr+"%");
			ps.setString(3, "%"+searchStr+"%");
			ps.setString(4, "%"+searchStr+"%");
			ps.setString(5, "%"+searchStr+"%");
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0�� post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / postsPerPage;
		searchlastPage = (searchTotalPost % postsPerPage == 0) ? searchlastPage : searchlastPage + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[ù �������Դϴ�.]");
			} else if (searchPage > searchlastPage) {
				searchPage = searchlastPage;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + searchPage + " / " + searchlastPage);
			}
		}
		
		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} 
		
		// post 10���� ������ �ϱ� ���� ������ -> ������������ �並 ���� �� ���µ� ���...?
		try {
			String sql = "SELECT * FROM ( "
					+ "select rownum no, np.* "
					+ "from ( SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "FROM post p, post_locations pl, hashtag h "
					+ "WHERE p.post_num = pl.post_num "
					+ "and h.post_num = p.post_num "
					+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
					+ "ORDER BY p.written_time DESC ) np ) "
					+ "WHERE no BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+searchStr+"%");
			ps.setString(2, "%"+searchStr+"%");
			ps.setString(3, "%"+searchStr+"%");
			ps.setString(4, "%"+searchStr+"%");
			ps.setString(5, "%"+searchStr+"%");
			ps.setInt(6, (searchPage - 1) * postsPerPage + 1);
			ps.setInt(7, searchPage * postsPerPage);
			rs = ps.executeQuery();

			System.out.println(
					" num  |                   name                   |         city          |         time        ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			int i = 1;
			while (rs.next()) {
				int pnum = rs.getInt(2);
				String name = rs.getString(3);
				String city = rs.getString(4);
				String time = rs.getString(5);
				if (mode == 2) { // ���� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // �Ϲ� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}// isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printSearchPostMenu() {
		System.out.println("1. ����  2. ���� 3. ���� �˻���� 4. ���� �˻����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����
			mode = 2;
			printSearchPost();
			break;
		case 2: // ����
			searchPage = 1;
			printMainMenu();
			break;
		case 3:
			searchPage--;
			printSearchPost();
			break;
		case 4:
			searchPage++;
			printSearchPost();
			break;
		}
	}
	
	public void printReply(int pnum) {
		printReplyTable(pnum);
		
		int rnum = 0;
		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ��۸� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printMainMenu();
			} else { // Reply ����
				try {
					// reply_num ����
					String sql = "select reply_num from ( "
							+ "select rownum no, np.* "
							+ "from (select t.nickname, r.text, r.written_time, r.p_reply_num, r.reply_num "
							+ "from reply r, traveler t "
							+ "where  r.traveler_num = t.num "
							+ "and r.post_num = ? "
							+ "order by r.written_time) np) where no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, pnum);
					ps.setInt(2, no + (replyPage - 1) * postsPerPage);
					ResultSet rs = ps.executeQuery();

					if (rs.next()) {
						rnum = rs.getInt(1);
					}
					mode = 1;

					if (rnum == 0) { // ����
						System.out.println("�߸� ���õǾ����ϴ�.");
						printReply(pnum);
					} else { // reply ����
						// 1. ��ȸ��
						// 1-1. ����
						// 2. ȸ��
						// 2-1. ��� �ۼ�
						// 2-2. ��� ����(�ڱ��)
						// 2-2. ��� ����(�ڱ��)
						// 2-3. �Ű�
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // �Ϲ� mode
			switch(user) {
			case 1: printReplyMenu_guest(pnum);
			case 2: printReplyMenu_traveler(pnum);
			}
		}
	}
	
	public void printReplyTable(int pnum) {
		int TotalReply = 0; // �� Reply ��
		int lastReply = 0; // ������ Reply ��
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalReply, lastReply ���
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ "(select t.nickname, r.text, r.written_time, r.p_reply_num "
					+ "from reply r, traveler t "
					+ "where  r.traveler_num = t.num "
					+ "and r.post_num = ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();
			if (rs.next())
				TotalReply = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lastReply = TotalReply / replyPerPage;
		lastReply = (TotalReply % replyPerPage == 0) ? lastReply : lastReply + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (replyPage < 1) {
				replyPage = 1;
				System.out.println("page: " + replyPage + " / " + lastReply + "\t[ù �������Դϴ�.]");
			} else if (replyPage > lastReply) {
				replyPage = lastReply;
				System.out.println("page: " + replyPage + " / " + lastReply + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + replyPage + " / " + lastReply);
			}
		}
		
		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} 
		
		try {
			String sql = "select * from ( "
					+ "select rownum no, np.* "
					+ "from (select t.nickname, r.text, r.written_time, r.p_reply_num "
					+ "from reply r, traveler t "
					+ "where  r.traveler_num = t.num "
					+ "and r.post_num = ? "
					+ "order by r.written_time) np)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();

			System.out.println(
					" nickname                |                   time                                    ");
			System.out.println("                        text                       ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			int i = 1;
			while (rs.next()) {
				String name = rs.getString(2);
				String text = rs.getString(3);
				String w_time = rs.getString(4);
				int parentReplyNum = rs.getInt(5);
				if(parentReplyNum == 0) {
					if (mode == 2) { // ���� mode	
						System.out.printf("%3d|\t%-30s\t%s\n%s\n", i, name, w_time, text);
						i++;
					} else { // �Ϲ� mode
						System.out.printf("%-30s\t%s\n%s\n", name, w_time, text);
					}
				}
				else {
					if (mode == 2) { // ���� mode	
						System.out.printf("\t\t%3d|\t%-30s\t%s\n\t\t%s\n", i, name, w_time, text);
						i++;
					} else { // �Ϲ� mode
						System.out.printf("\t\t%-30s\t%s\n\t\t%s\n", name, w_time, text);
					}
				}
				// ���� ����
			}// isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printReplyMenu_traveler(int pnum) {
		System.out.println("1. ����  2. �ۺ��� 3. ���� ��� 4. ���� ���");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����
			mode = 2;
			printReply(pnum);
			break;
		case 2: // ����
			replyPage = 1;
			printPost(pnum);
			break;
		case 3:
			searchPage--;
			printReply(pnum);
			break;
		case 4:
			searchPage++;
			printReply(pnum);
			break;
		}
	}
	
	public void printReplyMenu_guest(int pnum) {
		System.out.println("1. �ۺ��� 2. ���� ��� 3. ���� ���");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����
			replyPage = 1;
			printPost(pnum);
			break;
		case 2:
			searchPage--;
			printReply(pnum);
			break;
		case 3:
			searchPage++;
			printReply(pnum);
			break;
		}
	}
	
	public void printBookmarkTable() {
		System.out.println("�ϸ�ũ ���");
		
		int searchTotalPost = 0; // �� Post ��
		int searchlastPage = 0; // ������ Page
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		int Tnum = traveler.getTnum();
		// totalPost, lastPage ���
		try {
			String sql = "select count(*) from traveler_bookmarks where traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0�� post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / postsPerPage;
		searchlastPage = (searchTotalPost % postsPerPage == 0) ? searchlastPage : searchlastPage + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[ù �������Դϴ�.]");
			} else if (searchPage > searchlastPage) {
				searchPage = searchlastPage;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + searchPage + " / " + searchlastPage);
			}
		}
		
		// post 10���� ������ �ϱ� ���� ������
		try {
			String sql = "select * from (select rownum no, np.* "
					+ "from (select distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "from post p, post_locations pl, traveler_bookmarks b "
					+ "where p.post_num = pl.post_num "
					+ "and b.bookmark = p.post_num "
					+ "and b.traveler_num = ? "
					+ "order by p.written_time desc) np)"
					+ "where no between ? and ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			ps.setInt(2, (searchPage - 1) * postsPerPage + 1);
			ps.setInt(3, searchPage * postsPerPage);
			rs = ps.executeQuery();

			System.out.println(
					" num  |                   name                   |         city          |         time        ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			int i = 1;
			while (rs.next()) {
				int pnum = rs.getInt(2);
				String name = rs.getString(3);
				String city = rs.getString(4);
				String time = rs.getString(5);
				if (mode == 2) { // ���� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // �Ϲ� mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}// isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printBookmarkMenu() {
		// TODO: ClearConsole ����
		int Tnum = traveler.getTnum();
		// Post Table ǥ��
		printBookmarkTable();
		
		route = 2;

		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ����Ʈ�� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printBookmarkMenu();
			} else { // Post ����
				try {
					// pnum ����
					String sql = "select * from (select rownum no, np.* "
							+ "from (select distinct p.post_num, pl.name, pl.city, p.written_time "
							+ "from post p, post_locations pl, traveler_bookmarks b "
							+ "where p.post_num = pl.post_num "
							+ "and b.bookmark = p.post_num "
							+ "and b.traveler_num = ? "
							+ "order by p.written_time desc) np)"
							+ "where no between ? and ? "
							+ "and no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, Tnum);
					ps.setInt(2, (searchPage - 1) * postsPerPage + 1);
					ps.setInt(3, searchPage * postsPerPage);
					ps.setInt(4, no + (page - 1) * postsPerPage);
					ResultSet rs = ps.executeQuery();

					int pnum = 0;
					if (rs.next()) {
						pnum = rs.getInt(2);
					}
					mode = 1;

					if (pnum == 0) { // ����
						System.out.println("�߸� ���õǾ����ϴ�.");
						printBookmarkMenu();
					} else { // Post �� ǥ��
						printPost(pnum);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // �Ϲ� mode
			// User�� ���� �޴� ǥ��
			printBookmarkMenu_traveler();
		}
	}
	
	public void printBookmarkMenu_traveler() {
		System.out.println("1. ����  2. ����  3. ����  4. ���� �޴�");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����
			page--;
			printBookmarkMenu();
			break;
		case 2: // ����
			page++;
			printBookmarkMenu();
			break;
		case 3: // ����
			mode = 2;
			printBookmarkMenu();
			break;
		case 4: // ���� �޴�
			printMainMenu();
			break;
		}
	}

	
}
