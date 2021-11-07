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
	public static int userState = 1;	// 사용자 상태(1:비회원 2:TRAVELER 3:ADMIN)
	// changePage 함수에서 사용가능
	Connection conn;
	Statement stmt;
	int num;
	int count;	// 마지막 장인지 확인하기 위해서 page에서 rs.next()한 횟수 카운트 -> 10보다 작으면 마지막장 / 10이여도 마지막 일 수 있다(대안 생각)
	Scanner sc = new Scanner(System.in);
	String search;	// 검색 할 단어 입력 받는 문자열
	int[] postNum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};	// 선택 할때 실제 pNum 저장할 문자열
	
	// post 내용은 회원/비회원 구분없이 볼 수 있다. ADMIN계정 로그인시 안보임
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
	
	// 비회원 용 메인메뉴 선택뷰
	public void selectWork_ver_non() {
		System.out.println("1. 가입  2. 로그인  3. 이전  4. 다음  5. 선택  6. 검색  7. 종료");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");

		switch (num) {
		case 1:
			break;
		case 2:	// 로그인 성공할 시 userState 2(TRAVELER) or 3(ADMIN)으로 바꾸고 mainpage(conn, stmt) 호출
			// test
			userState = 2;	// TRAVELER
			break;
		case 3:		// 이전
			// 첫페이지 일때
			if(page == 1) {
				System.out.println("첫 장입니다.");
				selectWork_ver_non();
			}
			else {
				page--;
				mainpage(conn, stmt);
			}
			break;
		case 4:		// 다음
			// --------------마지막페이지 일때 확인하는 법
			if(count < 10) {
				System.out.println("마지막장입니다.");
				selectWork_ver_non();
			}
			else {
				page++;
				mainpage(conn, stmt);
			}
			break;
		case 5:		// 선택 - 선택화면으로 바꿀려면 rs 다시 가져올려면 try catch 쓰고 복잡해질 거 같아서 일단 위에서 부터 0~9 순이라 생각하고 입력받은 숫자의 포스트 선택(선택 화면 뷰 없음)
			System.out.print("몇번째 포스트를 선택하시겠습니까?");
			num = sc.nextInt();
			System.out.printf("\n\n");
			PostPage pp = new PostPage(conn, stmt, userState, postNum[num]);
			break;
		case 6:
			break;
		case 7:
			System.out.println("종료합니다.");
			break;
		}
	}
	
	// TRAVELER 용 메인메뉴 선택뷰
	public void selectWork_ver_mem() {
		System.out.println("1. 회원 정보 수정  2. 로그아웃  3. 이전  4. 다음  5. 선택  6. 검색  7. 포스트 쓰기 8. 북마크 9. 종료 ");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		System.out.printf("\n\n");

		switch (num) {
		case 1:
			break;
		case 2:	// 로그아웃 -> userState = 1
			userState = 1;
			mainpage(conn, stmt);
			break;
		case 3:		// 이전
			// 첫페이지 일때
			if(page == 1) {
				System.out.println("첫 장입니다.");
				selectWork_ver_non();
			}
			else {
				page--;
				mainpage(conn, stmt);
			}
			break;
		case 4:		// 다음
			// --------------마지막페이지 일때 확인하는 법
			if(count < 10) {
				System.out.println("마지막장입니다.");
				selectWork_ver_non();
			}
			else {
				page++;
				mainpage(conn, stmt);
			}
			break;
		case 5:		// 선택
			System.out.print("몇번째 포스트를 선택하시겠습니까?");
			num = sc.nextInt();
			System.out.printf("\n\n");
			PostPage pp = new PostPage(conn, stmt, userState, postNum[num]);
			break;
		case 6:		// 검색
			System.out.print("검색 내용을 입력하시오");
			search = sc.next();
			break;
		case 7:		// 포스트 쓰기
			break;
		case 8:		// 북마크
			break;
		case 9:		// 종료
			System.out.println("종료합니다.");
			break;
		}
	}
}