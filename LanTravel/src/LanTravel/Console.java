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
	int mode; // 1: 일반, 2: 선택
	boolean isPostWriter;
	boolean isReplyWriter;
	int searchPage = 1; // 검색용 page 번호
	int replyPage = 1;
	int reportPage = 1;
	int bookmarkPage = 1;
	int parentPage; // post 상세보기가 검색에서 선택되어 왔는지(0) 메인에서 선택되어 왔는지(1) 북마크에서 선택대어 왔는지(2) 구분 -> post
					// 상세보기에서 이전으로 돌아갈때 사용
	String searchStr = null;
	String type; // admin에서 타입이 R인지 P인지 구분용
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

	// 회원 가입 화면
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
			/**** 아이디 ****/
			while (true) {
				System.out.print("아이디를 입력하세요. ");
				id = sc.nextLine();

				sql = "SELECT id FROM traveler WHERE id = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();

				if (rs.next()) { // 중복 아이디가 존재하면 새로 받음
					System.out.println("아이디가 존재합니다.");
				} else
					break;
			}

			/**** 비밀번호 ****/
			System.out.print("비밀번호를 입력하세요. ");
			pw = sc.nextLine();

			/**** 닉네임 ****/
			while (true) {
				System.out.print("닉네임을 입력하세요. ");
				nickname = sc.nextLine();
				sql = "SELECT nickname FROM traveler WHERE nickname = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, nickname);
				rs = ps.executeQuery();

				if (rs.next()) {// 중복 닉네임이 존재하면 새로 받음
					System.out.println("닉네임이 존재합니다.");
				} else
					break;
			}

			/**** 이메일 ****/
			System.out.print("이메일을 입력하세요. ");
			email = sc.nextLine();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		guest.join(id, pw, nickname, email);

		System.out.println("회원가입이 완료되었습니다.");
		System.out.println("--------------------------------");
	}

	// 로그인 화면
	public void printLogin() {
		String id = null;
		String pw = null;

		System.out.println("1. 일반 로그인  2. 관리자 로그인");
		int menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // Traveler 로그인
			System.out.println("[일반 로그인]");
			System.out.print("아이디를 입력하세요: ");
			id = sc.nextLine();
			System.out.print("비밀번호를 입력하세요: ");
			pw = sc.nextLine();

			if (guest.loginTraveler(id, pw)) {
				System.out.println("로그인에 성공하였습니다.");
				userType = 2; // Traveler
				num = guest.getNum();
				traveler = new Traveler(conn, stmt);
				traveler.setNum(num);
			} else {
				System.out.println("로그인에 실패하였습니다.");
			}
			break;
		case 2: // Admin 로그인
			System.out.println("[관리자 로그인]");
			System.out.print("아이디를 입력하세요: ");
			id = sc.nextLine();
			System.out.print("비밀번호를 입력하세요: ");
			pw = sc.nextLine();

			if (guest.loginAdmin(id, pw)) {
				System.out.println("로그인에 성공하였습니다.");
				userType = 3; // Admin
				num = guest.getNum();
				admin = new Admin(conn, stmt);
				admin.setNum(num);
			} else {
				System.out.println("로그인에 실패하였습니다.");
			}
			break;
		}
	}

	// 회원 정보 수정 화면
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
		System.out.println("|                 회원정보수정                |");
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
		System.out.println("1. 비밀번호 수정  2. 닉네임 수정  3. 이메일 수정  4. 이전 화면");
		System.out.println("할 일을 선택하시오.");
		menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // 비밀번호 수정
			System.out.println("새로운 비밀번호를 입력하세요. (취소 : 0) : ");
			String newPw = sc.nextLine();
			if (newPw.equals("0")) {
				System.out.println("취소되었습니다.");
				printUpdate_traveler();
				return;
			}

			if (traveler.updatePassword(newPw))
				System.out.println("비밀번호가 수정되었습니다.");
			else
				System.out.println("오류!");
			break;
		case 2: // 닉네임 수정
			String newNickname = null;

			INPUT: while (true) {
				System.out.println("새로운 닉네임을 입력하세요. (취소 : 0) : ");
				newNickname = sc.nextLine();

				if (newNickname.equals("0")) {
					System.out.println("취소되었습니다.");
					printUpdate_traveler();
					return;
				}
				try {
					sql = "SELECT nickname FROM traveler WHERE nickname = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, newNickname);
					rs = ps.executeQuery();

					if (rs.next()) { // 중복 아이디가 존재하면 새로 받음
						System.out.println("닉네임이 존재합니다.");
					} else
						break INPUT;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (traveler.updateNickname(newNickname))
				System.out.println("닉네임이 수정되었습니다.");
			else
				System.out.println("오류!");
			break;
		case 3: // 이메일 수정
			System.out.println("새로운 이메일을 입력하세요. (취소 : 0) : ");
			String newEmail = sc.nextLine();
			if (newEmail.equals("0")) {
				System.out.println("취소되었습니다.");
				printUpdate_traveler();
				return;
			}

			if (traveler.updateNickname(newEmail))
				System.out.println("이메일이 수정되었습니다.");
			else
				System.out.println("오류!");
			break;
		case 4: // 이전 화면
			break;
		default:
			System.out.println("잘못 입력하였습니다.");
			break;
		}
	}

	// 관리자 정보 수정 화면
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
		System.out.println("|                 관리자정보수정              |");
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
		System.out.println("1. 비밀번호 수정  2. 이전 화면");
		System.out.println("할 일을 선택하시오.");
		menu = sc.nextInt();
		sc.nextLine();

		switch (menu) {
		case 1: // 비밀번호 수정
			System.out.println("새로운 비밀번호를 입력하세요. (취소 : 0) : ");
			String newPw = sc.nextLine();
			if (newPw.equals("0")) {
				System.out.println("취소되었습니다.");
				printUpdate_admin();
				return;
			}

			if (admin.updatePassword(newPw))
				System.out.println("비밀번호가 수정되었습니다.");
			else
				System.out.println("오류!");
			break;
		case 2: // 이전 화면
			break;
		default:
			System.out.println("잘못 입력하였습니다.");
			break;
		}
	}

	// Post 작성 화면
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
		System.out.println("|                 포스트 작성                 |");
		System.out.println("--------------------------------------------");

		int state = 0; // 0: 작성, 1: 등록, 2: 종료

		EXIT: while (true) {
			System.out.println("장소: " + country + " " + city + " " + name);
			System.out.println("여행 기간: " + start_date + " ~ " + end_date);
			System.out.println("본문: " + text);
			System.out.println("사진: " + picture);
			System.out.println("태그: " + tag);

			System.out.println("작성할 항목을 선택하세요.");
			System.out.println("1. 장소  2. 여행 기간  3. 본문  4. 사진  5. 태그  6. 등록  7. 종료");
			int menu = sc.nextInt();
			sc.nextLine();

			switch (menu) {
			case 1:
				System.out.println("여행 장소를 입력하세요.");
				System.out.print("1. 국가: ");
				country = sc.nextLine();
				System.out.print("2. 도시: ");
				city = sc.nextLine();
				System.out.print("3. 이름: ");
				name = sc.nextLine();
				break;
			case 2:
				System.out.println("여행 기간을 입력하세요. (yyyy-mm-dd)");
				System.out.print("1. 시작일: ");
				start_date = sc.nextLine();
				System.out.print("2. 종료일: ");
				end_date = sc.nextLine();
				break;
			case 3:
				System.out.println("본문을 입력하세요.");
				text = sc.nextLine();
				break;
			case 4:
				System.out.println("사진 경로를 입력하세요. (띄어쓰기로 구분)");
				picture = sc.nextLine();
				break;
			case 5:
				System.out.println("해시태그를 입력하세요. (띄어쓰기로 구분)");
				tag = sc.nextLine();
				break;
			case 6:
				state = 1; // 등록
				break;
			case 7:
				state = 2; // 종료
				break;
			}

			if (state == 1) { // 등록
				if (name == null || name.isEmpty()) {
					System.out.println("여행 장소가 비어 있습니다.");
					state = 0;
				} else {
					if (traveler.writePost(start_date, end_date, text, name, country, city, picture, tag))
						System.out.println("등록되었습니다.");
					else
						System.out.println("오류!");
					break EXIT;
				}
			} else if (state == 2) { // 종료
				System.out.println("종료합니다.");
				break EXIT;
			}
		}
	}

	// 신고 화면
	public void printReport(String type) {
		String reason = null;
		
		if (type.equalsIgnoreCase("P")) {
			System.out.println("포스트 신고");
			while (reason == null) {
				System.out.println("신고 사유를 입력하세요 (취소 : 0)");
				reason = sc.nextLine();
				if (reason.equals("0")) {
					System.out.println("취소되었습니다.");
					return;
				}
				if (reason == null)
					System.out.println("신고 사유가 공란입니다.");
			}
			traveler.reportPost(pnum, reason);
		}if (type.equalsIgnoreCase("R")) {
			System.out.println("댓글 신고");
			while (reason == null) {
				System.out.println("신고 사유를 입력하세요 (취소 : 0)");
				reason = sc.nextLine();
				if (reason.equals("0")) {
					System.out.println("취소되었습니다.");
					return;
				}
				if (reason == null)
					System.out.println("신고 사유가 공란입니다.");
			}
			traveler.reportReply(rnum, reason);
		}
	}

	// Post Table 화면
	public void printPostTable() {
		int totalPost = 0; // 총 Post 수
		int lastPage = 0; // 마지막 Page

		// totalPost, lastPage 계산
		try {
			String sql = "SELECT COUNT(post_num) FROM post";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				totalPost = rs.getInt(1) - 1; // -1 : 0번 post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lastPage = totalPost / linePerPage;
		lastPage = (totalPost % linePerPage == 0) ? lastPage : lastPage + 1;

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
		} else { // 일반 mode
			// Page 표시
			if (page < 1) {
				page = 1;
				System.out.println("page: " + page + " / " + lastPage + "\t[첫 페이지입니다.]");
			} else if (page > lastPage) {
				page = lastPage;
				System.out.println("page: " + page + " / " + lastPage + "\t[마지막 페이지입니다.]");
			} else {
				System.out.println("page: " + page + " / " + lastPage);
			}
		}

		// Post Table 표시
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", no - ((page - 1) * linePerPage), name, city,
							time);
				} else { // 일반 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 메인 메뉴 화면 - Guest
	public void printMainMenu_guest() {
		guest = new Guest(conn, stmt);

		System.out.println("1. 가입  2. 로그인  3. 이전  4. 다음  5. 선택  6. 검색  7. 종료");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 회원가입
			printJoin();
			printMainMenu();
			break;
		case 2: // 로그인 - 성공할 시 userType 2(TRAVELER) or 3(ADMIN)으로 바꾸고 printMainMenu() 호출
			printLogin();
			if (userType == 3) { // Admin
				printReportMenu();
			} else {
				printMainMenu();
			}
			break;
		case 3: // 이전
			page--;
			printMainMenu();
			break;
		case 4: // 다음
			page++;
			printMainMenu();
			break;
		case 5: // 선택
			mode = 2;
			printMainMenu();
			break;
		case 6: // 검색
			System.out.print("검색 내용을 입력하세요. ");
			searchStr = sc.next();
			printSearchPost();
			break;
		case 7:
			System.out.println("종료합니다.");
			System.exit(1);
			break;
		}
	}

	// 메인 메뉴 화면 - Traveler
	public void printMainMenu_traveler() {
		System.out.println("1. 회원 정보 수정  2. 로그아웃  3. 이전  4. 다음  5. 선택  6. 검색  7. 포스트 쓰기  8. 북마크  9. 종료 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 회원 정보 수정
			printUpdate_traveler();
			printMainMenu();
			break;
		case 2: // 로그아웃 -> userType = 1
			userType = 1; // Guest
			printMainMenu();
			break;
		case 3: // 이전
			page--;
			printMainMenu();
			break;
		case 4: // 다음
			page++;
			printMainMenu();
			break;
		case 5: // 선택
			mode = 2;
			printMainMenu();
			break;
		case 6: // 검색
			System.out.print("검색 내용을 입력하세요. ");
			searchStr = sc.next();
			printSearchPost();
			break;
		case 7: // 포스트 쓰기
			printWritePost();
			printMainMenu();
			break;
		case 8: // 북마크
			printBookmarkMenu();
			printMainMenu();
			break;
		case 9: // 종료
			System.out.println("종료합니다.");
			System.exit(1);
			break;
		}
	}

	// 메인 메뉴 화면 - Admin
	public void printMainMenu_admin() {
		System.out.println("1. 회원 정보 수정  2. 로그아웃  3. 이전  4. 다음  5. 선택  6. 종료 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			printUpdate_admin();
			printReportMenu();
			break;
		case 2: // 로그아웃 -> userType = 1
			userType = 1; // Guest
			printMainMenu();
			break;
		case 3: // 이전
			reportPage--;
			printReportMenu();
			break;
		case 4: // 다음
			reportPage++;
			printReportMenu();
			break;
		case 5: // 선택
			mode = 2;
			printReportMenu();
			break;
		case 6: // 종료
			System.out.println("종료합니다.");
			System.exit(1);
			break;
		}
	}

	// 메인 메뉴 화면
	public void printMainMenu() {
		// Post Table 화면
		printPostTable();

		parentPage = 1;

		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 포스트를 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printMainMenu();
			} else { // Post 선택
				try {
					// pnum 추출
					String sql = "SELECT post_num FROM post_view WHERE no = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, no + (page - 1) * linePerPage);
					ResultSet rs = ps.executeQuery();

					int pnum = 0;
					if (rs.next()) {
						pnum = rs.getInt(1);
					}
					mode = 1;

					if (pnum == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printMainMenu();
					} else { // Post 상세 표시
						printPost(pnum);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // 일반 mode
			// userType별 메인 메뉴 표시
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

	// Post 상세 표시 화면
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
				int writerNum = rs.getInt(9); // 작성자 확인 용도
				System.out.printf("작성자 : %s\n", nickname);
				System.out.printf("장소 : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("여행기간 : %s ~ %s\n", start_date, end_date);
				System.out.printf("작성 시간 : %s\n", w_time);
				System.out.printf("%s\n", text);

				isPostWriter = tnum == writerNum ? true : false;
			}

			sql = "SELECT h.tag_name " + "from post p, hashtag h " + "where h.post_num = p.post_num "
					+ "and p.Post_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			rs = ps.executeQuery();

			System.out.print("태그 : ");
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
			System.out.println("별점 : " + score);

			if (userType == 2) { // Traveler
				if (isPostWriter) { // 작성자
					printPostSelection_traveler_writer(pnum);
				} else { // 비작성자
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

	// Post 선택 화면 - Guest
	public void printPostSelection_guest(int pnum) {
		System.out.println("1. 이전 화면  2. 댓글 보기 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전화면
			if (parentPage == 1) // 메인
				printMainMenu();
			else if (parentPage == 0) // 검색
				printSearchPost();
			break;
		case 2: // 댓글보기
			printReply(pnum);
			break;
		}
	}

	// Post 선택 화면 - Traveler(비작성자)
	public void printPostSelection_traveler(int pnum) {
		System.out.println("1. 이전 화면  2. 댓글 작성  3. 댓글 보기  4. 평가  5. 신고  6. 북마크 등록/해제");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		sc.nextLine();
		System.out.printf("\n\n");
		this.pnum = pnum;

		switch (menu) {
		case 1: // 이전화면
			if (parentPage == 1) // 메인
				printMainMenu();
			else if (parentPage == 0) // 검색
				printSearchPost();
			else if (parentPage == 2) // 북마크
				printBookmarkMenu();
			break;
		case 2: // 댓글 작성
			// reply insert
			traveler.replyToPost(pnum);
			printPostSelection_traveler(pnum);
			break;
		case 3: // 댓글보기
			printReply(pnum);
			break;
		case 4: // 평가
			traveler.rating(pnum);
			printPostSelection_traveler(pnum);
			break;
		case 5: // 신고
			printReport("P");
			printPostSelection_traveler(pnum);
			break;
		case 6: // 북마크 등록/해제
			traveler.enrollBookmark(pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	// Post 선택 화면 - Traveler(작성자)
	public void printPostSelection_traveler_writer(int pnum) {
		System.out.println("1. 이전 화면  2. 수정  3. 삭제  4. 댓글 작성  5. 댓글 보기  6. 북마크 등록/해제");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		String sql;
		String newStr;

		switch (menu) {
		case 1: // 이전화면
			if (parentPage == 1)
				printMainMenu();
			else if (parentPage == 0)
				printSearchPost();
			break;
		case 2: // 수정
			System.out.println("수정 사항을 선택하시오.");
			System.out.println("1. 내용 2. 장소명");
			int n = sc.nextInt();

			switch (n) {
			case 1:
				try {
					System.out.println("내용 수정 : ");
					newStr = sc.next();
					sql = "update post set text = ? where post_num = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, newStr);
					ps.setInt(2, pnum);
					ResultSet rs = ps.executeQuery();
					System.out.println("수정되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				printPost(pnum);
				break;
			case 2:
				try {
					System.out.println("장소명 수정 : ");
					newStr = sc.next();
					sql = "update post_locations set name = ? where post_num = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, newStr);
					ps.setInt(2, pnum);
					ResultSet rs = ps.executeQuery();
					System.out.println("수정되었습니다.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				printPost(pnum);
				break;
			}
			break;
		case 3: // 삭제
			try {
				sql = "delete from post where post_num = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, pnum);
				ResultSet rs = ps.executeQuery();
				System.out.println("삭제되었습니다.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			printMainMenu();
			break;
		case 4: // 댓글 작성
			traveler.replyToPost(pnum);
			printPostSelection_traveler_writer(pnum);
			break;
		case 5: // 댓글 보기
			printReply(pnum);
			break;
		case 6: // 북마크 등록/해제
			traveler.enrollBookmark(pnum);
			printPostSelection_traveler_writer(pnum);
			break;
		}
	}

	// Post 선택 화면 - Admin
	public void printPostSelection_admin(int num) {
		System.out.println("1. 신고 목록  2. 삭제  3. 유지");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 신고 목록
			printReportMenu();
			break;
		case 2: // 삭제
			admin.DeletePostReply(type, num);
			printReportMenu();
			break;
		case 3: // 유지
			System.out.println("게시물을 유지합니다.");
			printReportMenu();
			break;
		}
	}

	// 검색 화면
	public void printSearchPost() {
		printSearchPostTable();
		parentPage = 0;

		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 포스트를 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printSearchPost();
			} else { // Post 선택
				try {
					// pnum 추출
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

					if (pnum == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printMainMenu();
					} else { // Post 상세 표시
						printPost(pnum);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			printSearchPostMenu();
	}

	// 검색 목록 화면
	public void printSearchPostTable() {
		System.out.println("[ " + searchStr + " ] 검색 결과입니다.");

		int searchTotalPost = 0; // 총 Post 수
		int searchlastPage = 0; // 마지막 Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage 계산
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

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
		} else { // 일반 mode
			// Page 표시
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[첫 페이지입니다.]");
			} else if (searchPage > searchlastPage) {
				searchPage = searchlastPage;
				System.out.println("page: " + searchPage + " / " + searchlastPage + "\t[마지막 페이지입니다.]");
			} else {
				System.out.println("page: " + searchPage + " / " + searchlastPage);
			}
		}

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // 일반 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 검색 메뉴 화면
	public void printSearchPostMenu() {
		System.out.println("1. 선택  2. 이전  3. 이전 검색 결과  4. 다음 검색 결과");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 선택
			mode = 2;
			printSearchPost();
			break;
		case 2: // 이전
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

	// 댓글(1개) 화면
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

				System.out.printf("작성자 : %s\n", nickname);
				System.out.printf("작성 시간 : %s\n", w_time);
				System.out.printf("%s\n", text);
				System.out.printf("상위 포스트 : %d\n", p_num);
			}

			printPostSelection_admin(num);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 댓글 화면
	public void printReply(int pnum) {
		int tnum = userType == 2 ? traveler.getNum() : -1;

		printReplyTable(pnum);

		int rnum = 0;
		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 댓글을 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			sc.nextLine();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printPost(pnum);
			} else { // Reply 선택
				try {
					// reply_num 추출
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
						int writerNum = rs.getInt(2); // 작성자 확인 용도
						this.rnum = rnum;
						isReplyWriter = tnum == writerNum ? true : false;
					}
					mode = 1;

					if (rnum == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printReply(pnum);
					} else { // reply 선택
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
							System.out.println("1. 댓글 작성  2. 댓글 수정  3. 댓글 삭제  4. 이전");
							int choice = sc.nextInt();
							sc.nextLine();
							switch (choice) {
							case 1: // 대댓글
								traveler.replyToReply(pnum, rnum);
								printReply(pnum);
								break;
							case 2:
								System.out.println("댓글을 입력하세요.");
								String text = sc.nextLine();
								sql = "UPDATE reply SET text = ?, written_time = SYSDATE WHERE reply_num = ?";
								ps = conn.prepareStatement(sql);
								ps.setString(1, text);
								ps.setInt(2, rnum);
								rs = ps.executeQuery();
								System.out.println("수정되었습니다.");
								printReply(pnum);
								break;
							case 3:
								sql = "DELETE FROM reply WHERE reply_num = ?";
								ps = conn.prepareStatement(sql);
								ps.setInt(1, rnum);
								rs = ps.executeQuery();
								System.out.println("삭제되었습니다.");
							case 4:
								printReply(pnum);
								break;
							}
						} else {
							System.out.println("1. 댓글 작성  2. 댓글 신고  3. 이전");
							int choice = sc.nextInt();
							sc.nextLine();
							switch (choice) {
							case 1:
								traveler.replyToReply(pnum, rnum);
								printReply(pnum);
								break;
							case 2: // 댓글 신고
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
		} else { // 일반 mode
			switch (userType) {
			case 1:
				printReplyMenu_guest(pnum);
			case 2:
				printReplyMenu_traveler(pnum);
			}
		}
	}

	// 댓글 목록 화면
	public void printReplyTable(int pnum) {
		int TotalReply = 0; // 총 Reply 수
		int lastReply = 0; // 마지막 Reply 수
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalReply, lastReply 계산
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

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
		} else { // 일반 mode
			// Page 표시
			if (replyPage < 1) {
				replyPage = 1;
				System.out.println("page: " + replyPage + " / " + lastReply + "\t[첫 페이지입니다.]");
			} else if (replyPage > lastReply) {
				replyPage = lastReply;
				System.out.println("page: " + replyPage + " / " + lastReply + "\t[마지막 페이지입니다.]");
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
					if (mode == 2) { // 선택 mode
						System.out.printf("%3d|\t%-30s\t%s\n[%s]\n", i, name, w_time, text);
						i++;
					} else { // 일반 mode
						System.out.printf("%-30s\t%s\n[%s]\n", name, w_time, text);
					}
				} else {
					if (mode == 2) { // 선택 mode
						System.out.printf("\t\t%3d|\t%-30s\t%s\n\t\t[%s]\n", i, name, w_time, text);
						i++;
					} else { // 일반 mode
						System.out.printf("\t\t%-30s\t%s\n\t\t[%s]\n", name, w_time, text);
					}
				}
				// 대댓글 구현
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 댓글 메뉴 화면 - Traveler
	public void printReplyMenu_traveler(int pnum) {
		System.out.println("1. 선택  2. 글 보기  3. 이전 댓글  4. 다음 댓글");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 선택
			mode = 2;
			printReply(pnum);
			break;
		case 2: // 이전
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

	// 댓글 메뉴 화면 - Guest
	public void printReplyMenu_guest(int pnum) {
		System.out.println("1. 글보기 2. 이전 댓글 3. 다음 댓글");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전
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

	// 북마크 목록 화면
	public void printBookmarkTable() {
		System.out.println("북마크 목록");

		int searchTotalPost = 0; // 총 Post 수
		int searchLastPage = 0; // 마지막 Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		int Tnum = traveler.getNum();
		// totalPost, lastPage 계산
		try {
			String sql = "SELECT COUNT(*) FROM traveler_bookmarks WHERE traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0번 post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchLastPage = searchTotalPost / linePerPage;
		searchLastPage = (searchTotalPost % linePerPage == 0) ? searchLastPage : searchLastPage + 1;

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
		} else { // 일반 mode
			// Page 표시
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage + "\t[첫 페이지입니다.]");
			} else if (searchPage > searchLastPage) {
				searchPage = searchLastPage;
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage + "\t[마지막 페이지입니다.]");
			} else {
				System.out.println("page: " + bookmarkPage + " / " + searchLastPage);
			}
		}

		// post 10개씩 나오게 하기 위한 쿼리문
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // 일반 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", pnum, name, city, time);
				}
			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 북마크 메뉴 화면 - Traveler
	public void printBookmarkMenu_traveler() {
		System.out.println("1. 이전  2. 다음  3. 선택  4. 메인 메뉴");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전
			page--;
			printBookmarkMenu();
			break;
		case 2: // 다음
			page++;
			printBookmarkMenu();
			break;
		case 3: // 선택
			mode = 2;
			printBookmarkMenu();
			break;
		case 4: // 메인 메뉴
			printMainMenu();
			break;
		}
	}

	// 북마크 메뉴 화면
	public void printBookmarkMenu() {
		int Tnum = traveler.getNum();
		// Post Table 표시
		printBookmarkTable();

		parentPage = 2;

		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 포스트를 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printBookmarkMenu();
			} else { // Post 선택
				try {
					// pnum 추출
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

					if (pnum == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printBookmarkMenu();
					} else { // Post 상세 표시
						printPost(pnum);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // 일반 mode
			// userType별 메인 메뉴 표시
			printBookmarkMenu_traveler();
		}
	}

	// 신고 목록 화면
	public void printReportTable() {
		System.out.println("신고 목록");

		int searchTotalPost = 0; // 총 Post 수
		int searchlastPage = 0; // 마지막 Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage 계산
		try {
			String sql = "select count(*) " + "from report r, record rc, post p, reply rp "
					+ "where r.report_num = rc.report_num " + "and rc.post_num = p.post_num "
					+ "and rp.reply_num = rc.reply_num ";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0번 post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / linePerPage;
		searchlastPage = (searchTotalPost % linePerPage == 0) ? searchlastPage : searchlastPage + 1;

		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
		} else { // 일반 mode
			// Page 표시
			if (searchPage < 1) {
				searchPage = 1;
				System.out.println("page: " + reportPage + " / " + searchlastPage + "\t[첫 페이지입니다.]");
			} else if (searchPage > searchlastPage) {
				searchPage = searchlastPage;
				System.out.println("page: " + reportPage + " / " + searchlastPage + "\t[마지막 페이지입니다.]");
			} else {
				System.out.println("page: " + reportPage + " / " + searchlastPage);
			}
		}

		// post 10개씩 나오게 하기 위한 쿼리문
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%12d | %-1s\t|  %4d\t | %-40s| %s\n", i, type, num, reason, text);
					i++;
				} else { // 일반 mode
					System.out.printf("%12d | %-1s\t|  %4d\t | %-40s| %s\n", rpnum, type, num, reason, text);
				}
			} // isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 신고 메뉴 화면
	public void printReportMenu() {
		// Report Table 표시
		printReportTable();

		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 포스트를 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printReportMenu();
			} else { // Post 선택
				try {
					// pnum 추출
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

					if (num == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printReportMenu();
					} else { // Post 상세 표시
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
		} else { // 일반 mode
			// userType별 메인 메뉴 표시
			printMainMenu_admin();
		}
	}
}
