// traveler�� ���ϴ� ����� �ۼ��� �ż����� Ŭ����
package LanTravel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Traveler {
	Scanner sc = new Scanner(System.in);
	Connection conn;
	Statement stmt;

	private int num;

	public Traveler() {
		this.conn = null;
		this.stmt = null;
		num = -1;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	// ��й�ȣ ����
	public boolean updatePassword(String pw) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;

		try {
			sql = "UPDATE traveler SET pw = ? WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, pw);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// �г��� ����
	public boolean updateNickname(String nickname) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;

		try {
			sql = "UPDATE traveler SET nickname = ? WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, nickname);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// �̸��� ����
	public boolean updateEmail(String email) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = null;

		try {
			sql = "UPDATE traveler SET email = ? WHERE num = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// �ϸ�ũ ���/����
	public void enrollBookmark(int pnum) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			sql = "SELECT * FROM traveler_bookmarks WHERE traveler_num = ? AND bookmark = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, num);
			ps.setInt(2, pnum);
			rs = ps.executeQuery();

			if (rs.next()) {
				sql = "DELETE FROM traveler_bookmarks WHERE traveler_num = ? AND bookmark = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("�ϸ�ũ�� �����Ǿ����ϴ�.");
			} else {
				sql = "INSERT INTO traveler_bookmarks VALUES(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("�ϸ�ũ�� ��ϵǾ����ϴ�.");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ����Ʈ �ۼ�
	public boolean writePost(String start_date, String end_date, String text, String name, String country, String city,
			String picture, String tag) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int num = 0;
		int pnum = 0;

		try {
			// pnum ����
			sql = "SELECT MAX(post_num) FROM post";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				pnum = rs.getInt(1) + 1;
			}

			/**** Post ��� ****/
			sql = "INSERT INTO post VALUES(?, TO_DATE(?, 'rrrr-mm-dd'), TO_DATE(?, 'rrrr-mm-dd'), ?, SYSDATE, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setString(2, start_date);
			ps.setString(3, end_date);
			ps.setString(4, text);
			ps.setInt(5, num);
			rs = ps.executeQuery();

			/**** ��� ��� ****/
			sql = "INSERT INTO post_locations VALUES(?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setString(2, name);
			ps.setString(3, country);
			ps.setString(4, city);
			rs = ps.executeQuery();

			/**** ���� ��� ****/
			if (picture != null) {
				String[] pictokens = picture.split(" ");
				int picnum = 0;

				for (int i = 0; i < pictokens.length; i++) {
					// picnum ����
					sql = "SELECT COUNT(*) FROM post_pictures";
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if (rs.next()) {
						picnum = rs.getInt(1) + 1;
					}

					sql = "INSERT INTO  post_pictures VALUES(?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, pnum);
					ps.setInt(2, picnum);
					ps.setString(3, pictokens[i]);
					rs = ps.executeQuery();
				}
			}

			/**** �±� ��� ****/
			if (tag != null) {
				String[] tagtokens = tag.split(" #");
				int tagnum = 0;

				for (int i = 0; i < tagtokens.length; i++) {
					// tagnum ����
					sql = "SELECT COUNT(*) FROM hashtag";
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if (rs.next()) {
						tagnum = rs.getInt(1) + 1;
					}

					sql = "INSERT INTO hashtag VALUES(?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, pnum);
					ps.setInt(2, tagnum);
					ps.setString(3, tagtokens[i]);
					rs = ps.executeQuery();
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// �Ű�
	public void reportPost(int pnum, String reason) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rep_num = 0;

		System.out.println("����Ʈ �Ű�");

		try {
			// �Ű� ��ȣ ����
			sql = "SELECT max(report_num) FROM report";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}

			sql = "INSERT INTO report VALUES(?, ?, ?, ?, 1)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rep_num);
			ps.setString(2, "P");
			ps.setString(3, reason);
			ps.setInt(4, pnum);
			rs = ps.executeQuery();

			// ����Ʈ �Ű�
			sql = "INSERT INTO record VALUES(?, 0, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setInt(2, rep_num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			System.out.println("�Ű� �Ϸ�Ǿ����ϴ�.");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	public void reportReply(int rnum, String reason) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rep_num = 0;

		System.out.println("��� �Ű�");

		try {
			// �Ű� ��ȣ ����
			sql = "SELECT max(report_num) FROM report";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}

			sql = "INSERT INTO report VALUES(?, ?, ?, ?, 1)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rep_num);
			ps.setString(2, "R");
			ps.setString(3, reason);
			ps.setInt(4, rnum);
			rs = ps.executeQuery();

			// ����Ʈ �Ű�
			sql = "INSERT INTO record VALUES(0, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rnum);
			ps.setInt(2, rep_num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			System.out.println("�Ű� �Ϸ�Ǿ����ϴ�.");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	// ��� �ۼ�
	public void replyToPost(int pnum) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String reply = null;
		int rep_num = 0;

		System.out.println("��� �ۼ� (��� : 0) ");

		try {
			// rep_num ����
			sql = "SELECT MAX(reply_num) FROM reply";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}

			while (reply == null || reply.isEmpty()) {
				System.out.println("����� �Է��ϼ���.");
				reply = sc.nextLine();
				if (reply.equals("0")) {
					break;
				}
				if (reply == null || reply.isEmpty())
					System.out.println("����� �����Դϴ�.");
			}
			if (reply.equals("0")) {
				System.out.println("��ҵǾ����ϴ�.");
			} else {
				sql = "INSERT INTO reply VALUES(?, ?, SYSDATE, ?, null, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rep_num);
				ps.setString(2, reply);
				ps.setInt(3, num);
				ps.setInt(4, pnum);
				rs = ps.executeQuery();

				System.out.println("����� �ۼ��Ͽ����ϴ�.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ���� �ۼ�
		public void replyToReply(int pnum, int parentNum) {
			Database db = new Database();
			conn = db.getConnection();
			stmt = db.getStatement();
			String sql = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			String reply = null;
			int rep_num = 0;

			System.out.println("��� �ۼ� (��� : 0) ");

			try {
				// rep_num ����
				sql = "SELECT MAX(reply_num) FROM reply";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					rep_num = rs.getInt(1) + 1;
				}

				while (reply == null || reply.isEmpty()) {
					System.out.println("����� �Է��ϼ���.");
					reply = sc.nextLine();
					if (reply.equals("0")) {
						break;
					}
					if (reply == null || reply.isEmpty())
						System.out.println("����� �����Դϴ�.");
				}
				if (reply.equals("0")) {
					System.out.println("��ҵǾ����ϴ�.");
				} else {
					sql = "INSERT INTO reply VALUES(?, ?, SYSDATE, ?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, rep_num);
					ps.setString(2, reply);
					ps.setInt(3, num);
					ps.setInt(4, parentNum);
					ps.setInt(5, pnum);
					rs = ps.executeQuery();

					System.out.println("����� �ۼ��Ͽ����ϴ�.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	// ��
	public void rating(int pnum) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rate = 0;
		boolean isUploaded = false;

		System.out.println("���� �ۼ�");

		try {
			sql = "SELECT * FROM rating WHERE post_num = ? AND traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("�̹� ������ �ۼ��Ͽ����ϴ�.");
			} else {
				while (!isUploaded) {
					System.out.println("������ �Է��ϼ���.(1, 2, 3, 4, 5), (���: 0)");
					rate = sc.nextInt();
					sc.nextLine();

					switch (rate) {
					case 0:
						System.out.println("��ҵǾ����ϴ�.");
						return;
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
						sql = "INSERT INTO rating VALUES(?, ?, ?)";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, pnum);
						ps.setInt(2, num);
						ps.setInt(3, rate);
						rs = ps.executeQuery();
						System.out.println("������ �ݿ��Ǿ����ϴ�.");
						isUploaded = true;
						break;
					default:
						System.out.println("�߸��� ���Դϴ�.");
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}