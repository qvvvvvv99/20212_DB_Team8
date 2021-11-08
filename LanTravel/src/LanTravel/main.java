package LanTravel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class main {
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_NAME ="project";	// tb??
	public static final String USER_PASSWD ="comp322";
	
	public static void main(String[] args) {
		Console cs = new Console();
		
		cs.printMainMenu();
		
		
//		mainpage mp = new mainpage();
//		mp.mainpage(conn, stmt);
	}
}