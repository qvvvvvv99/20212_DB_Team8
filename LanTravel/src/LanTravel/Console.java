package LanTravel;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Console {
	public static final int linePerPage = 10;
	Database db = null;
	Connection conn = null;
	Statement stmt = null;

	Guest guest;
	Traveler traveler;
	Admin admin;

	Scanner sc = new Scanner(System.in);
	int userType; // 1: Guest, 2: Traveler, 3: Admin
	int num; // (userType 1: -1), 2: tNum, 3: aNum
	int page;
	int mode; // 1: �Ϲ�, 2: ����
	boolean isPostWriter;
	boolean isReplyWriter;
	int searchPage = 1; // �˻��� page ��ȣ
	int replyPage = 1;
	int reportPage = 1;
	int bookmarkPage = 1;
	int parentPage; // post �󼼺��Ⱑ �˻����� ���õǾ� �Դ���(0) ���ο��� ���õǾ� �Դ���(1) �ϸ�ũ���� ���ô�� �Դ���(2) ���� -> post
					// �󼼺��⿡�� �������� ���ư��� ���
	String searchStr = null;
	String type; // admin���� Ÿ���� R���� P���� ���п�
	int pnum;
	int rnum;

	public Console() {
		db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();

		userType = 1;
		page = 1;
		mode = 1;
		isPostWriter = false;
		isReplyWriter = false;
	}

	// ȸ�� ���� ȭ��
	public void printJoin() {
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String id = null;
		String pw = null;
		String nickname = null;
		String email = null;

		try {
			sc.nextLine();
			/**** ���̵� ****/
			while (true) {
				System.out.print("���̵� �Է��ϼ���. ");
				id = sc.nextLine();

				sql = "SELECT id FROM traveler WHERE id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();

				if (rs.next()) { // �ߺ� ���̵� �����ϸ� ���� ����
					System.out.println("���̵� �����մϴ�.");
				} else
					break;
			}

			/**** ��й�ȣ ****/
			System.out.print("��й�ȣ�� �Է��ϼ���. ");
			pw = sc.nextLine();

			/**** �г��� ****/
			while (true) {
				System.out.print("�г����� �Է��ϼ���. ");
				nickname = sc.nextLine();
				sql = "SELECT nickname FROM traveler WHERE nickname = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, nickname);
				rs = ps.executeQuery();

				if (rs.next()) {// �ߺ� �г����� �����ϸ� ���� ����
					System.out.println("�г����� �����մϴ�.");
				} else
					break;
			}

			/**** �̸��� ****/
			System.out.print("�̸����� �Է��ϼ���. ");
			email = sc.nextLine();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		guest.join(id, pw, nickname, email);

		System.out.println("ȸ�������� �Ϸ�Ǿ����ϴ�.");
		System.out.println("--------------------------------");
	}

	// �α��� ȭ��
	public void printLogin() {
		String id = null;
		String pw = null;

		System.out.println("1. �Ϲ� �α���  2. ������ �α���");
		int menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // Traveler �α���
			System.out.println("[�Ϲ� �α���]");
			System.out.print("���̵� �Է��ϼ���: ");
			id = sc.nextLine();
			System.out.print("��й�ȣ�� �Է��ϼ���: ");
			pw = sc.nextLine();

			if (guest.loginTraveler(id, pw)) {
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
				userType = 2; // Traveler
				num = guest.getNum();
				traveler = new Traveler(conn, stmt);
				traveler.setNum(num);
			} else {
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
			}
			break;
		case 2: // Admin �α���
			System.out.println("[������ �α���]");
			System.out.print("���̵� �Է��ϼ���: ");
			id = sc.nextLine();
			System.out.print("��й�ȣ�� �Է��ϼ���: ");
			pw = sc.nextLine();

			if (guest.loginAdmin(id, pw)) {
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
				userType = 3; // Admin
				num = guest.getNum();
				admin = new Admin(conn, stmt);
				admin.setNum(num);
			} else {
				System.out.println("�α��ο� �����Ͽ����ϴ�.");
			}
			break;
		}
	}

	// ȸ�� ���� ���� ȭ��
	public void printUpdate_traveler() {
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String id = null;
		String pw = null;
		String nickname = null;
		String email = null;

		int menu;

		System.out.println("--------------------------------------------");
		System.out.println("|                 ȸ����������                |");
		System.out.println("--------------------------------------------");

		try {
			sql = "SELECT * FROM traveler WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getString(2);
				pw = rs.getString(3);
				nickname = rs.getString(4);
				email = rs.getString(5);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("ID       : " + id);
		System.out.println("PW       : " + pw);
		System.out.println("nickname : " + nickname);
		System.out.println("email    : " + email);
		System.out.println("--------------------------------------------");
		System.out.println("1. ��й�ȣ ����  2. �г��� ����  3. �̸��� ����  4. ���� ȭ��");
		System.out.println("�� ���� �����Ͻÿ�.");
		menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // ��й�ȣ ����
			System.out.println("���ο� ��й�ȣ�� �Է��ϼ���. (��� : 0) : ");
			String newPw = sc.nextLine();
			if (newPw.equals("0")) {
				System.out.println("��ҵǾ����ϴ�.");
				printUpdate_traveler();
				return;
			}

			if (traveler.updatePassword(newPw))
				System.out.println("��й�ȣ�� �����Ǿ����ϴ�.");
			else
				System.out.println("����!");
			break;
		case 2: // �г��� ����
			String newNickname = null;

			INPUT: while (true) {
				System.out.println("���ο� �г����� �Է��ϼ���. (��� : 0) : ");
				newNickname = sc.nextLine();

				if (newNickname.equals("0")) {
					System.out.println("��ҵǾ����ϴ�.");
					printUpdate_traveler();
					return;
				}
				try {
					sql = "SELECT nickname FROM traveler WHERE nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, newNickname);
					rs = ps.executeQuery();

					if (rs.next()) { // �ߺ� ���̵� �����ϸ� ���� ����
						System.out.println("�г����� �����մϴ�.");
					} else
						break INPUT;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (traveler.updateNickname(newNickname))
				System.out.println("�г����� �����Ǿ����ϴ�.");
			else
				System.out.println("����!");
			break;
		case 3: // �̸��� ����
			System.out.println("���ο� �̸����� �Է��ϼ���. (��� : 0) : ");
			String newEmail = sc.nextLine();
			if (newEmail.equals("0")) {
				System.out.println("��ҵǾ����ϴ�.");
				printUpdate_traveler();
				return;
			}

			if (traveler.updateNickname(newEmail))
				System.out.println("�̸����� �����Ǿ����ϴ�.");
			else
				System.out.println("����!");
			break;
		case 4: // ���� ȭ��
			break;
		default:
			System.out.println("�߸� �Է��Ͽ����ϴ�.");
			break;
		}
	}

	// ������ ���� ���� ȭ��
	public void printUpdate_admin() {
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String id = null;
		String pw = null;
		String nickname = null;
		String email = null;

		int menu;

		System.out.println("--------------------------------------------");
		System.out.println("|                 ��������������              |");
		System.out.println("--------------------------------------------");

		try {
			sql = "SELECT * FROM admin WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getString(2);
				pw = rs.getString(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("ID       : " + id);
		System.out.println("PW       : " + pw);
		System.out.println("--------------------------------------------");
		System.out.println("1. ��й�ȣ ����  2. ���� ȭ��");
		System.out.println("�� ���� �����Ͻÿ�.");
		menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // ��й�ȣ ����
			System.out.println("���ο� ��й�ȣ�� �Է��ϼ���. (��� : 0) : ");
			String newPw = sc.nextLine();
			if (newPw.equals("0")) {
				System.out.println("��ҵǾ����ϴ�.");
				printUpdate_admin();
				return;
			}

			if (admin.updatePassword(newPw))
				System.out.println("��й�ȣ�� �����Ǿ����ϴ�.");
			else
				System.out.println("����!");
			break;
		case 2: // ���� ȭ��
			break;
		default:
			System.out.println("�߸� �Է��Ͽ����ϴ�.");
			break;
		}
	}

	// Post �ۼ� ȭ��
	public void printWritePost() {
		int pnum = 0;
		String start_date = null;
		String end_date = null;
		String text = null;
		String name = null;
		String country = null;
		String city = null;
		String picture = null;
		String tag = null;

		System.out.println("--------------------------------------------");
		System.out.println("|                 ����Ʈ �ۼ�                 |");
		System.out.println("--------------------------------------------");

		int state = 0; // 0: �ۼ�, 1: ���, 2: ����

		EXIT: while (true) {
			System.out.println("���: " + country + " " + city + " " + name);
			System.out.println("���� �Ⱓ: " + start_date + " ~ " + end_date);
			System.out.println("����: " + text);
			System.out.println("����: " + picture);
			System.out.println("�±�: " + tag);

			System.out.println("�ۼ��� �׸��� �����ϼ���.");
			System.out.println("1. ���  2. ���� �Ⱓ  3. ����  4. ����  5. �±�  6. ���  7. ����");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
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
				System.out.println("���� �Ⱓ�� �Է��ϼ���. (yyyy-mm-dd)");
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
				System.out.println("���� ��θ� �Է��ϼ���. (����� ����)");
				picture = sc.nextLine();
				break;
			case 5:
				System.out.println("�ؽ��±׸� �Է��ϼ���. (����� ����)");
				tag = sc.nextLine();
				break;
			case 6:
				state = 1; // ���
				break;
			case 7:
				state = 2; // ����
				break;
			}

			if (state == 1) { // ���
				if (name == null || name.isEmpty()) {
					System.out.println("���� ��Ұ� ��� �ֽ��ϴ�.");
					state = 0;
				} else {
					if (traveler.writePost(start_date, end_date, text, name, country, city, picture, tag))
						System.out.println("��ϵǾ����ϴ�.");
					else
						System.out.println("����!");
					break EXIT;
				}
			} else if (state == 2) { // ����
				System.out.println("�����մϴ�.");
				break EXIT;
			}
		}
	}

	// �Ű� ȭ��
	public void printReport(String type) {
		String reason = null;
		
		if (type.equalsIgnoreCase("P")) {
			System.out.println("����Ʈ �Ű�");
			while (reason == null) {
				System.out.println("�Ű� ������ �Է��ϼ��� (��� : 0)");
				reason = sc.nextLine();
				if (reason.equals("0")) {
					System.out.println("��ҵǾ����ϴ�.");
					return;
				}
				if (reason == null)
					System.out.println("�Ű� ������ �����Դϴ�.");
			}
			traveler.reportPost(pnum, reason);
		}if (type.equalsIgnoreCase("R")) {
			System.out.println("��� �Ű�");
			while (reason == null) {
				System.out.println("�Ű� ������ �Է��ϼ��� (��� : 0)");
				reason = sc.nextLine();
				if (reason.equals("0")) {
					System.out.println("��ҵǾ����ϴ�.");
					return;
				}
				if (reason == null)
					System.out.println("�Ű� ������ �����Դϴ�.");
			}
			traveler.reportReply(rnum, reason);
		}
	}

	// Post Table ȭ��
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
		lastPage = totalPost / linePerPage;
		lastPage = (totalPost % linePerPage == 0) ? lastPage : lastPage + 1;

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
			String sql = "SELECT * FROM (SELECT ROWNUM no, np.* FROM (SELECT p.post_num, pl.name, pl.city, p.written_time FROM post p, post_locations pl "
					+ "WHERE p.post_num = pl.post_num ORDER BY p.written_time DESC) np) WHERE no BETWEEN ? AND ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, (page - 1) * linePerPage + 1);
			ps.setInt(2, page * linePerPage);
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
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", no - ((page - 1) * linePerPage), name, city,
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

	// ���� �޴� ȭ�� - Guest
	public void printMainMenu_guest() {
		guest = new Guest(conn, stmt);

		System.out.println("1. ����  2. �α���  3. ����  4. ����  5. ����  6. �˻�  7. ����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ȸ������
			printJoin();
			printMainMenu();
			break;
		case 2: // �α��� - ������ �� userType 2(TRAVELER) or 3(ADMIN)���� �ٲٰ� printMainMenu() ȣ��
			printLogin();
			if (userType == 3) { // Admin
				printReportMenu();
			} else {
				printMainMenu();
			}
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
		case 7:
			System.out.println("�����մϴ�.");
			System.exit(1);
			break;
		}
	}

	// ���� �޴� ȭ�� - Traveler
	public void printMainMenu_traveler() {
		System.out.println("1. ȸ�� ���� ����  2. �α׾ƿ�  3. ����  4. ����  5. ����  6. �˻�  7. ����Ʈ ����  8. �ϸ�ũ  9. ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ȸ�� ���� ����
			printUpdate_traveler();
			printMainMenu();
			break;
		case 2: // �α׾ƿ� -> userType = 1
			userType = 1; // Guest
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
			printWritePost();
			printMainMenu();
			break;
		case 8: // �ϸ�ũ
			printBookmarkMenu();
			printMainMenu();
			break;
		case 9: // ����
			System.out.println("�����մϴ�.");
			System.exit(1);
			break;
		}
	}

	// ���� �޴� ȭ�� - Admin
	public void printMainMenu_admin() {
		System.out.println("1. ȸ�� ���� ����  2. �α׾ƿ�  3. ����  4. ����  5. ����  6. ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			printUpdate_admin();
			printReportMenu();
			break;
		case 2: // �α׾ƿ� -> userType = 1
			userType = 1; // Guest
			printMainMenu();
			break;
		case 3: // ����
			reportPage--;
			printReportMenu();
			break;
		case 4: // ����
			reportPage++;
			printReportMenu();
			break;
		case 5: // ����
			mode = 2;
			printReportMenu();
			break;
		case 6: // ����
			System.out.println("�����մϴ�.");
			System.exit(1);
			break;
		}
	}

	// ���� �޴� ȭ��
	public void printMainMenu() {
		// Post Table ȭ��
		printPostTable();

		parentPage = 1;

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
					ps.setInt(1, no + (page - 1) * linePerPage);
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
			// userType�� ���� �޴� ǥ��
			switch (userType) {
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

	// Post �� ǥ�� ȭ��
	public void printPost(int pnum) {
		ResultSet rs = null;
		int tnum = userType == 2 ? traveler.getNum() : -1;

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
				int writerNum = rs.getInt(9); // �ۼ��� Ȯ�� �뵵
				System.out.printf("�ۼ��� : %s\n", nickname);
				System.out.printf("��� : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("����Ⱓ : %s ~ %s\n", start_date, end_date);
				System.out.printf("�ۼ� �ð� : %s\n", w_time);
				System.out.printf("%s\n", text);

				isPostWriter = tnum == writerNum ? true : false;
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

			if (userType == 2) { // Traveler
				if (isPostWriter) { // �ۼ���
					printPostSelection_traveler_writer(pnum);
				} else { // ���ۼ���
					printPostSelection_traveler(pnum);
				}
			} else if (userType == 3) { // Admin
				printPostSelection_admin(pnum);
			} else { // Guest (userType == 1)
				printPostSelection_guest(pnum);
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Post ���� ȭ�� - Guest
	public void printPostSelection_guest(int pnum) {
		System.out.println("1. ���� ȭ��  2. ��� ���� ");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // ����ȭ��
			if (parentPage == 1) // ����
				printMainMenu();
			else if (parentPage == 0) // �˻�
				printSearchPost();
			break;
		case 2: // ��ۺ���
			printReply(pnum);
			break;
		}
	}

	// Post ���� ȭ�� - Traveler(���ۼ���)
	public void printPostSelection_traveler(int pnum) {
		System.out.println("1. ���� ȭ��  2. ��� �ۼ�  3. ��� ����  4. ��  5. �Ű�  6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		sc.nextLine();
		System.out.printf("\n\n");
		this.pnum = pnum;

		switch (menu) {
		case 1: // ����ȭ��
			if (parentPage == 1) // ����
				printMainMenu();
			else if (parentPage == 0) // �˻�
				printSearchPost();
			else if (parentPage == 2) // �ϸ�ũ
				printBookmarkMenu();
			break;
		case 2: // ��� �ۼ�
			// reply insert
			traveler.replyToPost(pnum);
			printPostSelection_traveler(pnum);
			break;
		case 3: // ��ۺ���
			printReply(pnum);
			break;
		case 4: // ��
			traveler.rating(pnum);
			printPostSelection_traveler(pnum);
			break;
		case 5: // �Ű�
			printReport("P");
			printPostSelection_traveler(pnum);
			break;
		case 6: // �ϸ�ũ ���/����
			traveler.enrollBookmark(pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	// Post ���� ȭ�� - Traveler(�ۼ���)
	public void printPostSelection_traveler_writer(int pnum) {
		System.out.println("1. ���� ȭ��  2. ����  3. ����  4. ��� �ۼ�  5. ��� ����  6. �ϸ�ũ ���/����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		String sql;
		String newStr;

		switch (menu) {
		case 1: // ����ȭ��
			if (parentPage == 1)
				printMainMenu();
			else if (parentPage == 0)
				printSearchPost();
			break;
		case 2: // ����
			System.out.println("���� ������ �����Ͻÿ�.");
			System.out.println("1. ���� 2. ��Ҹ�");
			int n = sc.nextInt();

			switch (n) {
			case 1:
				try {
					System.out.println("���� ���� : ");
					newStr = sc.next();
					sql = "update post set text = ? where post_num = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, newStr);
					ps.setInt(2, pnum);
					ResultSet rs = ps.executeQuery();
					System.out.println("�����Ǿ����ϴ�.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				printPost(pnum);
				break;
			case 2:
				try {
					System.out.println("��Ҹ� ���� : ");
					newStr = sc.next();
					sql = "update post_locations set name = ? where post_num = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, newStr);
					ps.setInt(2, pnum);
					ResultSet rs = ps.executeQuery();
					System.out.println("�����Ǿ����ϴ�.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				printPost(pnum);
				break;
			}
			break;
		case 3: // ����
			try {
				sql = "delete from post where post_num = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ResultSet rs = ps.executeQuery();
				System.out.println("�����Ǿ����ϴ�.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			printMainMenu();
			break;
		case 4: // ��� �ۼ�
			traveler.replyToPost(pnum);
			printPostSelection_traveler_writer(pnum);
			break;
		case 5: // ��� ����
			printReply(pnum);
			break;
		case 6: // �ϸ�ũ ���/����
			traveler.enrollBookmark(pnum);
			printPostSelection_traveler_writer(pnum);
			break;
		}
	}

	// Post ���� ȭ�� - Admin
	public void printPostSelection_admin(int num) {
		System.out.println("1. �Ű� ���  2. ����  3. ����");
		System.out.print("�� ���� �����ϼ���. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // �Ű� ���
			printReportMenu();
			break;
		case 2: // ����
			admin.DeletePostReply(type, num);
			printReportMenu();
			break;
		case 3: // ����
			System.out.println("�Խù��� �����մϴ�.");
			printReportMenu();
			break;
		}
	}

	// �˻� ȭ��
	public void printSearchPost() {
		printSearchPostTable();
		parentPage = 0;

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
					String sql = "SELECT post_num FROM ( " + "select rownum no, np.* "
							+ "from ( SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
							+ "FROM post p, post_locations pl, hashtag h " + "WHERE p.post_num = pl.post_num "
							+ "and h.post_num = p.post_num "
							+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
							+ "ORDER BY p.written_time DESC ) np ) WHERE no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, "%" + searchStr + "%");
					ps.setString(2, "%" + searchStr + "%");
					ps.setString(3, "%" + searchStr + "%");
					ps.setString(4, "%" + searchStr + "%");
					ps.setString(5, "%" + searchStr + "%");
					ps.setInt(6, no + (searchPage - 1) * linePerPage);
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
		} else
			printSearchPostMenu();
	}

	// �˻� ��� ȭ��
	public void printSearchPostTable() {
		System.out.println("[ " + searchStr + " ] �˻� ����Դϴ�.");

		int searchTotalPost = 0; // �� Post ��
		int searchlastPage = 0; // ������ Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage ���
		try {
			String sql = "SELECT COUNT(*) FROM " + "(SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "FROM post p, post_locations pl, hashtag h " + "WHERE p.post_num = pl.post_num "
					+ "and h.post_num = p.post_num "
					+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
					+ "ORDER BY p.written_time DESC)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%" + searchStr + "%");
			ps.setString(2, "%" + searchStr + "%");
			ps.setString(3, "%" + searchStr + "%");
			ps.setString(4, "%" + searchStr + "%");
			ps.setString(5, "%" + searchStr + "%");
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / linePerPage;
		searchlastPage = (searchTotalPost % linePerPage == 0) ? searchlastPage : searchlastPage + 1;

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

		try {
			String sql = "SELECT * FROM ( " + "select rownum no, np.* "
					+ "from ( SELECT distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "FROM post p, post_locations pl, hashtag h " + "WHERE p.post_num = pl.post_num "
					+ "and h.post_num = p.post_num "
					+ "and (p.text like ? or pl.name like ? or pl.country like ? or pl.city like ? or h.tag_name like ?) "
					+ "ORDER BY p.written_time DESC ) np ) " + "WHERE no BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%" + searchStr + "%");
			ps.setString(2, "%" + searchStr + "%");
			ps.setString(3, "%" + searchStr + "%");
			ps.setString(4, "%" + searchStr + "%");
			ps.setString(5, "%" + searchStr + "%");
			ps.setInt(6, (searchPage - 1) * linePerPage + 1);
			ps.setInt(7, searchPage * linePerPage);
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
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// �˻� �޴� ȭ��
	public void printSearchPostMenu() {
		System.out.println("1. ����  2. ����  3. ���� �˻� ���  4. ���� �˻� ���");
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

	// ���(1��) ȭ��
	public void printOneReply(int num) {
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			String sql = "SELECT t.nickname, r.text, r.written_time, r.post_num FROM reply r, traveler t WHERE t.num = r.traveler_num AND r.reply_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			rs = ps.executeQuery();

			if (rs.next()) {
				String nickname = rs.getString(1);
				String text = rs.getString(2);
				String w_time = rs.getString(3);
				int p_num = rs.getInt(4);

				System.out.printf("�ۼ��� : %s\n", nickname);
				System.out.printf("�ۼ� �ð� : %s\n", w_time);
				System.out.printf("%s\n", text);
				System.out.printf("���� ����Ʈ : %d\n", p_num);
			}

			printPostSelection_admin(num);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ��� ȭ��
	public void printReply(int pnum) {
		int tnum = userType == 2 ? traveler.getNum() : -1;

		printReplyTable(pnum);

		int rnum = 0;
		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ����� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			sc.nextLine();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printPost(pnum);
			} else { // Reply ����
				try {
					// reply_num ����
					String sql = "select reply_num, num from ( " + "select rownum no, np.* "
							+ "from (select t.num, t.nickname, r.text, r.written_time, r.p_reply_num, r.reply_num "
							+ "from reply r, traveler t " + "where  r.traveler_num = t.num " + "and r.post_num = ? "
							+ "order by r.written_time) np) where no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, pnum);
					ps.setInt(2, no + (replyPage - 1) * linePerPage);
					ResultSet rs = ps.executeQuery();

					if (rs.next()) {
						rnum = rs.getInt(1);
						int writerNum = rs.getInt(2); // �ۼ��� Ȯ�� �뵵
						this.rnum = rnum;
						isReplyWriter = tnum == writerNum ? true : false;
					}
					mode = 1;

					if (rnum == 0) { // ����
						System.out.println("�߸� ���õǾ����ϴ�.");
						printReply(pnum);
					} else { // reply ����
						sql = "select t.nickname, r.text, r.written_time, t.num " + "from reply r, traveler t "
								+ "where r.traveler_num = t.num " + "and r.reply_num = ?";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, rnum);
						rs = ps.executeQuery();

						if (rs.next()) {
							String name = rs.getString(1);
							String rText = rs.getString(2);
							String w_time = rs.getString(3);
							int trav_num = rs.getInt(4);

							System.out.printf("%-30s\t%s\n[%s]\n", name, w_time, rText);
						}

						if (isReplyWriter) {
							System.out.println("1. ��� �ۼ�  2. ��� ����  3. ��� ����  4. ����");
							int choice = sc.nextInt();
							sc.nextLine();
							switch (choice) {
							case 1: // ����
								traveler.replyToReply(pnum, rnum);
								printReply(pnum);
								break;
							case 2:
								System.out.println("����� �Է��ϼ���.");
								String text = sc.nextLine();
								sql = "UPDATE reply SET text = ?, written_time = SYSDATE WHERE reply_num = ?";
								ps = conn.prepareStatement(sql);
								ps.setString(1, text);
								ps.setInt(2, rnum);
								rs = ps.executeQuery();
								System.out.println("�����Ǿ����ϴ�.");
								printReply(pnum);
								break;
							case 3:
								sql = "DELETE FROM reply WHERE reply_num = ?";
								ps = conn.prepareStatement(sql);
								ps.setInt(1, rnum);
								rs = ps.executeQuery();
								System.out.println("�����Ǿ����ϴ�.");
							case 4:
								printReply(pnum);
								break;
							}
						} else {
							System.out.println("1. ��� �ۼ�  2. ��� �Ű�  3. ����");
							int choice = sc.nextInt();
							sc.nextLine();
							switch (choice) {
							case 1:
								traveler.replyToReply(pnum, rnum);
								printReply(pnum);
								break;
							case 2: // ��� �Ű�
								printReport("R");
								printReply(pnum);
								break;
							case 3:
								printReply(pnum);
								break;
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // �Ϲ� mode
			switch (userType) {
			case 1:
				printReplyMenu_guest(pnum);
			case 2:
				printReplyMenu_traveler(pnum);
			}
		}
	}

	// ��� ��� ȭ��
	public void printReplyTable(int pnum) {
		int TotalReply = 0; // �� Reply ��
		int lastReply = 0; // ������ Reply ��
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalReply, lastReply ���
		try {
			String sql = "SELECT COUNT(*) FROM " + "(select t.nickname, r.text, r.written_time, r.p_reply_num "
					+ "from reply r, traveler t " + "where  r.traveler_num = t.num " + "and r.post_num = ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();
			if (rs.next())
				TotalReply = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lastReply = TotalReply / linePerPage;
		lastReply = (TotalReply % linePerPage == 0) ? lastReply : lastReply + 1;

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

		try {
			String sql = "select * from ( " + "select rownum no, np.* "
					+ "from (select t.nickname, r.text, r.written_time, r.p_reply_num " + "from reply r, traveler t "
					+ "where r.traveler_num = t.num " + "and r.post_num = ? " + "order by r.written_time) np)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();

			System.out.println(" nickname                |                   time                                    ");
			System.out.println("                        text                       ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			int i = 1;
			while (rs.next()) {
				String name = rs.getString(2);
				String text = rs.getString(3);
				String w_time = rs.getString(4);
				int parentReplyNum = rs.getInt(5);
				if (parentReplyNum == 0) {
					if (mode == 2) { // ���� mode
						System.out.printf("%3d|\t%-30s\t%s\n[%s]\n", i, name, w_time, text);
						i++;
					} else { // �Ϲ� mode
						System.out.printf("%-30s\t%s\n[%s]\n", name, w_time, text);
					}
				} else {
					if (mode == 2) { // ���� mode
						System.out.printf("\t\t%3d|\t%-30s\t%s\n\t\t[%s]\n", i, name, w_time, text);
						i++;
					} else { // �Ϲ� mode
						System.out.printf("\t\t%-30s\t%s\n\t\t[%s]\n", name, w_time, text);
					}
				}
				// ���� ����
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ��� �޴� ȭ�� - Traveler
	public void printReplyMenu_traveler(int pnum) {
		System.out.println("1. ����  2. �� ����  3. ���� ���  4. ���� ���");
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

	// ��� �޴� ȭ�� - Guest
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

	// �ϸ�ũ ��� ȭ��
	public void printBookmarkTable() {
		System.out.println("�ϸ�ũ ���");

		int searchTotalPost = 0; // �� Post ��
		int searchLastPage = 0; // ������ Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		int Tnum = traveler.getNum();
		// totalPost, lastPage ���
		try {
			String sql = "SELECT COUNT(*) FROM traveler_bookmarks WHERE traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0�� post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchLastPage = searchTotalPost / linePerPage;
		searchLastPage = (searchTotalPost % linePerPage == 0) ? searchLastPage : searchLastPage + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage + "\t[ù �������Դϴ�.]");
			} else if (searchPage > searchLastPage) {
				searchPage = searchLastPage;
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage);
			}
		}

		// post 10���� ������ �ϱ� ���� ������
		try {
			String sql = "select * from (select rownum no, np.* "
					+ "from (select distinct p.post_num, pl.name, pl.city, p.written_time "
					+ "from post p, post_locations pl, traveler_bookmarks b " + "where p.post_num = pl.post_num "
					+ "and b.bookmark = p.post_num " + "and b.traveler_num = ? " + "order by p.written_time desc) np)"
					+ "where no between ? and ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			ps.setInt(2, (bookmarkPage - 1) * linePerPage + 1);
			ps.setInt(3, bookmarkPage * linePerPage);
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
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// �ϸ�ũ �޴� ȭ�� - Traveler
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

	// �ϸ�ũ �޴� ȭ��
	public void printBookmarkMenu() {
		int Tnum = traveler.getNum();
		// Post Table ǥ��
		printBookmarkTable();

		parentPage = 2;

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
							+ "where p.post_num = pl.post_num " + "and b.bookmark = p.post_num "
							+ "and b.traveler_num = ? " + "order by p.written_time desc) np)"
							+ "where no between ? and ? " + "and no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, Tnum);
					ps.setInt(2, (bookmarkPage - 1) * linePerPage + 1);
					ps.setInt(3, bookmarkPage * linePerPage);
					ps.setInt(4, no + (bookmarkPage - 1) * linePerPage);
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
			// userType�� ���� �޴� ǥ��
			printBookmarkMenu_traveler();
		}
	}

	// �Ű� ��� ȭ��
	public void printReportTable() {
		System.out.println("�Ű� ���");

		int searchTotalPost = 0; // �� Post ��
		int searchlastPage = 0; // ������ Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage ���
		try {
			String sql = "select count(*) " + "from report r, record rc, post p, reply rp "
					+ "where r.report_num = rc.report_num " + "and rc.post_num = p.post_num "
					+ "and rp.reply_num = rc.reply_num ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0�� post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / linePerPage;
		searchlastPage = (searchTotalPost % linePerPage == 0) ? searchlastPage : searchlastPage + 1;

		if (mode == 2) { // ���� mode
			System.out.println("���� ����Դϴ�.");
		} else { // �Ϲ� mode
			// Page ǥ��
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + reportPage + " / " + searchlastPage + "\t[ù �������Դϴ�.]");
			} else if (searchPage > searchlastPage) {
				searchPage = searchlastPage;
				System.out.println("page: " + reportPage + " / " + searchlastPage + "\t[������ �������Դϴ�.]");
			} else {
				System.out.println("page: " + reportPage + " / " + searchlastPage);
			}
		}

		// post 10���� ������ �ϱ� ���� ������
		try {
			String sql = "select * from (select rownum no, np.* "
					+ "from (select distinct r.report_num, r.type, p.post_num, p.text text1, rp.reply_num, rp.text text2, r.reason "
					+ "from report r, record rc, post p, reply rp " + "where r.report_num = rc.report_num "
					+ "and rc.post_num = p.post_num " + "and rp.reply_num = rc.reply_num "
					+ "order by report_num desc) np) " + "where no between ? and ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, (reportPage - 1) * linePerPage + 1);
			ps.setInt(2, reportPage * linePerPage);
			rs = ps.executeQuery();

			System.out.println(
					" report num  |   type  |   num   |            reason            |             text            ");
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
			int i = 1;
			int num = 0;
			String text = null;
			while (rs.next()) {
				int rpnum = rs.getInt(2);
				String type = rs.getString(3);
				if (type.equals("P")) {
					num = rs.getInt(4);
					text = rs.getString(5);
					if (num == 0)
						type = "R";
				}
				String reason = rs.getString(8);
				if (type.equals("R")) {
					num = rs.getInt(6);
					text = rs.getString(7);
					if (num == 0) {
						type = "P";
						num = rs.getInt(4);
						text = rs.getString(5);
					}
				}
				if (mode == 2) { // ���� mode
					System.out.printf("%12d | %-1s\t|  %4d\t | %-40s| %s\n", i, type, num, reason, text);
					i++;
				} else { // �Ϲ� mode
					System.out.printf("%12d | %-1s\t|  %4d\t | %-40s| %s\n", rpnum, type, num, reason, text);
				}
			} // isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// �Ű� �޴� ȭ��
	public void printReportMenu() {
		// Report Table ǥ��
		printReportTable();

		if (mode == 2) { // ���� mode
			System.out.print("�� ��° ����Ʈ�� �����Ͻðڽ��ϱ�? (��� : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // ���
				mode = 1;
				printReportMenu();
			} else { // Post ����
				try {
					// pnum ����
					String sql = "select * from (select rownum no, np.* "
							+ "from (select distinct r.report_num, r.type, p.post_num, p.text text1, rp.reply_num, rp.text text2 "
							+ "from report r, record rc, post p, reply rp " + "where r.report_num = rc.report_num "
							+ "and rc.post_num = p.post_num " + "and rp.reply_num = rc.reply_num "
							+ "order by report_num desc) np) " + "where no between ? and ? " + "and no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, (reportPage - 1) * linePerPage + 1);
					ps.setInt(2, reportPage * linePerPage);
					ps.setInt(3, no + (reportPage - 1) * linePerPage);
					ResultSet rs = ps.executeQuery();

					type = null;
					int num = 0;
					if (rs.next()) {
						type = rs.getString(3);
						if (type.equals("P")) {
							num = rs.getInt(4);
							System.out.println("num " + num);
							if (num == 0)
								type = "R";
						}
						if (type.equals("R")) {
							num = rs.getInt(6);
							System.out.println("num " + num);
							if (num == 0) {
								type = "P";
								num = rs.getInt(4);
								System.out.println("num " + num);
							}
						}
					}
					mode = 1;

					if (num == 0) { // ����
						System.out.println("�߸� ���õǾ����ϴ�.");
						printReportMenu();
					} else { // Post �� ǥ��
						if (type.equalsIgnoreCase("P")) {
							printPost(num);
						}
						if (type.equalsIgnoreCase("R")) {
							printOneReply(num);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // �Ϲ� mode
			// userType�� ���� �޴� ǥ��
			printMainMenu_admin();
		}
	}
}
