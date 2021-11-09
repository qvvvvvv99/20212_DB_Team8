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
}
