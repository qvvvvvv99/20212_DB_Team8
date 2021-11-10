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
	int mode; // 1: 일반, 2: 선택
	boolean isWriter;
	int searchPage = 1;	// 검색용 page 번호
	int replyPage = 1;
	int route;	// post 상세보기가 검색에서 선택되어 왔는지(0) 메인에서 선택되어 왔는지(1) 북마크에서 선택대어 왔는지(2) 구분 -> post 상세보기에서 이전으로 돌아갈때 사용

	public Console() {
		db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();

		user = 1;
		page = 1;
		mode = 1;
		isWriter = false;
	}

	// Post Table 표시
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
		lastPage = totalPost / postsPerPage;
		lastPage = (totalPost % postsPerPage == 0) ? lastPage : lastPage + 1;

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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", no - ((page - 1) * postsPerPage), name, city,
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

	public void printMainMenu() {
		// TODO: ClearConsole 구현

		// Post Table 표시
		printPostTable();
		
		route = 1;

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
					ps.setInt(1, no + (page - 1) * postsPerPage);
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
			// User별 메인 메뉴 표시
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

	// Guest 메인 메뉴
	public void printMainMenu_guest() {
		guest = new Guest();

		System.out.println("1. 가입  2. 로그인  3. 이전  4. 다음  5. 선택  6. 검색  7. 종료");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			guest.join(conn, stmt);
			printMainMenu();
			break;
		case 2: // 로그인 성공할 시 user 2(TRAVELER) or 3(ADMIN)으로 바꾸고 printMainMenu() 호출
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
		case 6:	// 검색
			System.out.print("검색 내용을 입력하세요. ");
			searchStr = sc.next();
			printSearchPost();
			break;
		case 7:
			System.out.println("종료합니다.");
			break;
		}
	}

	// Traveler 메인 메뉴
	public void printMainMenu_traveler() {
		System.out.println("1. 회원 정보 수정  2. 로그아웃  3. 이전  4. 다음  5. 선택  6. 검색  7. 포스트 쓰기  8. 북마크  9. 종료 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			traveler.update(conn, stmt);
			printMainMenu();
			break;
		case 2: // 로그아웃 -> user = 1
			user = 1;
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
			traveler.write_post(conn, stmt);
			printMainMenu();
			break;
		case 8: // 북마크
			printBookmarkMenu();
			printMainMenu();
			break;
		case 9: // 종료
			System.out.println("종료합니다.");
			break;
		}
	}
	
	//admin 메인메뉴
	public void printMainMenu_admin() {
		System.out.println("1. 회원 정보 수정  2. 로그아웃  3. 신고된 목록 조회  4. 종료 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1:
			admin.update(conn, stmt);
			printMainMenu();
			break;
		case 2: // 로그아웃 -> user = 1
			user = 1;
			printMainMenu();
			break;
		case 3: // 이전
			page--;
			printMainMenu();
			break;
		case 4: // 종료
			System.out.println("종료합니다.");
			break;
		}
	}

	// Post 상세 표시
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
				int tnum = rs.getInt(9); // 작성자인지 확인 용도
				System.out.printf("작성자 : %s\n", nickname);
				System.out.printf("장소 : %s %s %s\n", loc_country, loc_city, loc_name);
				System.out.printf("여행기간 : %s ~ %s\n", start_date, end_date);
				System.out.printf("작성 시간 : %s\n", w_time);
				System.out.printf("%s\n", text);
				// if(tnum == 현재로그인한 회원 번호) 비회원이면 0
				// isWriter = True;
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

			if (user == 2) { // Traveler
				if (isWriter) { // 작성자인지
					printPostSelection_traveler_writer(pnum);
				} else { // 비작성자
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
		System.out.println("1. 이전 화면  2. 댓글 보기 ");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전화면
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
			break;
		case 2: // 댓글보기
			printReply(pnum);
			break;
		}
	}

	public void printPostSelection_traveler(int pnum) {
		System.out.println("1. 이전 화면  2. 댓글 작성  3. 댓글 보기  4. 평가  5. 신고  6. 북마크 등록/해제");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전화면
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
			else if(route == 2)
				printBookmarkMenu();
			break;
		case 2: // 댓글 작성
			// reply insert
			traveler.reply_to_post(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		case 3: // 댓글보기
			printReply(pnum);
			break;
		case 4: // 평가
			traveler.rating(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		case 5: // 신고
			// report insert
			traveler.report(conn, stmt, pnum, "P");
			printPostSelection_traveler(pnum);
			break;
		case 6: // 북마크 등록/해제
			traveler.enroll_bookmark(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	public void printPostSelection_traveler_writer(int pnum) {
		System.out.println("1. 이전 화면  2. 수정  3. 삭제  4. 댓글 작성  5. 댓글 보기  6. 북마크 등록/해제");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 이전화면
			if(route == 1)
				printMainMenu();
			else if(route == 0)
				printSearchPost();
			break;
		case 2: // 수정
			// post 수정 view
			break;
		case 3: // 삭제
			// post delete
			break;
		case 4: // 댓글 작성
			// reply insert
			break;
		case 5: // 댓글 보기
			printReply(pnum);
			break;
		case 6: // 북마크 등록/해제
			traveler.enroll_bookmark(conn, stmt, pnum);
			printPostSelection_traveler(pnum);
			break;
		}
	}

	public void printPostSelection_admin() {
		System.out.println("1. 신고 목록  2. 삭제  3. 유지");
		System.out.print("할 일을 선택하세요. ");
		int menu = sc.nextInt();
		System.out.printf("\n\n");

		switch (menu) {
		case 1: // 신고 목록
			// 신고목록view
			break;
		case 2: // 삭제
			// post delete
			break;
		case 3: // 유지
			System.out.println("게시물을 유지합니다.");
			printMainMenu();
			break;
		}
	}
	
	// 검색
	public void printSearchPost() {
		printSearchPostTable();
		route = 0;
		
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
		}
		else
			printSearchPostMenu();
	}
	
	public void printSearchPostTable() {
		System.out.println("[ " + searchStr + " ] 검색 결과입니다.");
		
		int searchTotalPost = 0; // 총 Post 수
		int searchlastPage = 0; // 마지막 Page
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalPost, lastPage 계산
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
				searchTotalPost = rs.getInt(1); // -1 : 0번 post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / postsPerPage;
		searchlastPage = (searchTotalPost % postsPerPage == 0) ? searchlastPage : searchlastPage + 1;

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
		
		// post 10개씩 나오게 하기 위한 쿼리문 -> 동적쿼리여서 뷰를 만들 수 없는데 어떻게...?
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // 일반 mode
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
		System.out.println("1. 선택  2. 이전 3. 이전 검색결과 4. 다음 검색결과");
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
	
	public void printReply(int pnum) {
		printReplyTable(pnum);
		
		int rnum = 0;
		if (mode == 2) { // 선택 mode
			System.out.print("몇 번째 댓글를 선택하시겠습니까? (취소 : 0) ");
			int no = sc.nextInt();
			System.out.printf("\n\n");

			if (no == 0) { // 취소
				mode = 1;
				printMainMenu();
			} else { // Reply 선택
				try {
					// reply_num 추출
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

					if (rnum == 0) { // 오류
						System.out.println("잘못 선택되었습니다.");
						printReply(pnum);
					} else { // reply 선택
						// 1. 비회원
						// 1-1. 이전
						// 2. 회원
						// 2-1. 댓글 작성
						// 2-2. 댓글 수정(자기거)
						// 2-2. 댓글 삭제(자기거)
						// 2-3. 신고
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else { // 일반 mode
			switch(user) {
			case 1: printReplyMenu_guest(pnum);
			case 2: printReplyMenu_traveler(pnum);
			}
		}
	}
	
	public void printReplyTable(int pnum) {
		int TotalReply = 0; // 총 Reply 수
		int lastReply = 0; // 마지막 Reply 수
		ResultSet rs = null;
		PreparedStatement ps = null;

		// totalReply, lastReply 계산
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
		
		if (mode == 2) { // 선택 mode
			System.out.println("선택 모드입니다.");
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
					if (mode == 2) { // 선택 mode	
						System.out.printf("%3d|\t%-30s\t%s\n%s\n", i, name, w_time, text);
						i++;
					} else { // 일반 mode
						System.out.printf("%-30s\t%s\n%s\n", name, w_time, text);
					}
				}
				else {
					if (mode == 2) { // 선택 mode	
						System.out.printf("\t\t%3d|\t%-30s\t%s\n\t\t%s\n", i, name, w_time, text);
						i++;
					} else { // 일반 mode
						System.out.printf("\t\t%-30s\t%s\n\t\t%s\n", name, w_time, text);
					}
				}
				// 대댓글 구현
			}// isWriter = True;

			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printReplyMenu_traveler(int pnum) {
		System.out.println("1. 선택  2. 글보기 3. 이전 댓글 4. 다음 댓글");
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
	
	public void printBookmarkTable() {
		System.out.println("북마크 목록");
		
		int searchTotalPost = 0; // 총 Post 수
		int searchlastPage = 0; // 마지막 Page
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		int Tnum = traveler.getTnum();
		// totalPost, lastPage 계산
		try {
			String sql = "select count(*) from traveler_bookmarks where traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Tnum);
			rs = ps.executeQuery();
			if (rs.next())
				searchTotalPost = rs.getInt(1); // -1 : 0번 post
		} catch (SQLException e) {
			e.printStackTrace();
		}
		searchlastPage = searchTotalPost / postsPerPage;
		searchlastPage = (searchTotalPost % postsPerPage == 0) ? searchlastPage : searchlastPage + 1;

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
		
		// post 10개씩 나오게 하기 위한 쿼리문
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
				if (mode == 2) { // 선택 mode
					System.out.printf("%5d | %-30s\t | %-15s\t | %20s\n", i, name, city, time);
					i++;
				} else { // 일반 mode
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
		// TODO: ClearConsole 구현
		int Tnum = traveler.getTnum();
		// Post Table 표시
		printBookmarkTable();
		
		route = 2;

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
			// User별 메인 메뉴 표시
			printBookmarkMenu_traveler();
		}
	}
	
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

	
}
