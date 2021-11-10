package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;
	private int Anum;
	
	public Admin(){
		conn = null;
		stmt = null;
		Anum = 0;
	}
	
	public void setNum(int Anum) {
		this.Anum = Anum;
	}
	
	public void update(Connection conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;
		
		String id = "";
		String pw = "";
		String input = "";
		
		int num;
		
		try {
			System.out.println("--------------------------------------------");
			System.out.println("|                 ��������������                |");
			System.out.println("--------------------------------------------");
			
			sql = "Select * from admin where num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Anum);
			rs = ps.executeQuery();
	
			if (rs.next()) {
				id = rs.getString(2);
				pw = rs.getString(3);
			}
			
			System.out.println("ID       : " + id);
			System.out.println("PW       : " + pw);
			System.out.println("--------------------------------------------");
			System.out.println("1. ��й�ȣ ����  2. ���� ȭ��");
			System.out.println("�� ���� �����Ͻÿ�.");
			num = sc.nextInt();
			sc.nextLine();
			
			switch(num) {
			case 1: //��й�ȣ ����
				System.out.println("���ο� ��й�ȣ�� �Է��ϼ��� : ");
				input = sc.nextLine();
				
				sql = "update admin set pw = ? where num = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ps.setInt(2, Anum);
				rs = ps.executeQuery();
				break;
			
			case 2:
				break;
				
			default:
				System.out.println("�߸� �Է��Ͽ����ϴ�.");
				break;
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}