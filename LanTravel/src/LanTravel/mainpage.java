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
		
		System.out.println("1. ����  2. �α���  3. ����  4. ����  5. ����  6. �˻�  7. ����");
		System.out.print("�� ���� �����Ͻÿ�.");
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
			System.out.println("�����մϴ�.");
			System.exit(1);
		}
		
		rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
