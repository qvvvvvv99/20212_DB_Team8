package LanTravel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class mainpage {
	public void mainpage(Connection conn, Statement stmt){
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		int num;
		
		try {
		String sql = "SELECT * from post_view where no between 1 and 10";
		rs = stmt.executeQuery(sql);
		
		System.out.println("num | name | country | time");
		System.out.println("-----------------------------");
		while(rs.next()) {
			// Fill out your code
			int Pnum = rs.getInt(2);
			String name = rs.getString(3);
			String country = rs.getString(4);
			String time = rs.getString(5);
			System.out.println(Pnum + " | " + name + " | " + country + " | " + time);
		}
		
		System.out.println("1. 가입  2. 로그인  3. 이전  4. 다음  5. 선택  6. 검색  7. 종료");
		System.out.print("할 일을 선택하시오.");
		num = sc.nextInt();
		
		switch(num) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			System.out.println("종료합니다.");
			System.exit(1);
		}
		
		rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
