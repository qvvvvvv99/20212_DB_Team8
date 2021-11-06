package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class mainpage {
	public static final int postsPerPage = 10;
	
	public void mainpage(Connection conn, Statement stmt) {
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		int num;

		try {
			String sql = "SELECT * from post_view where no between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			int page = 1;
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
				System.out.printf("%5d | %s\t\t| %s\t| %20s\n", Pnum, name, city, time);
			}

			System.out.println("1. ����  2. �α���  3. ����  4. ����  5. ����  6. �˻�  7. ����");
			System.out.print("�� ���� �����Ͻÿ�.");
			num = sc.nextInt();

			switch (num) {
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

			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
