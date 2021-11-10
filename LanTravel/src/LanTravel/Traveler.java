// traveler가 지니는 기능을 작성한 매서드의 클래스
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

	// 비밀번호 수정
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

	// 닉네임 수정
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

	// 이메일 수정
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

	// 북마크 등록/해제
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
				System.out.println("북마크가 해제되었습니다.");
			} else {
				sql = "INSERT INTO traveler_bookmarks VALUES(?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, num);
				ps.setInt(2, pnum);
				rs = ps.executeQuery();
				System.out.println("북마크가 등록되었습니다.");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 포스트 작성
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
			// pnum 지정
			sql = "SELECT MAX(post_num) FROM post";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				pnum = rs.getInt(1) + 1;
			}

			/**** Post 등록 ****/
			sql = "INSERT INTO post VALUES(?, TO_DATE(?, 'rrrr-mm-dd'), TO_DATE(?, 'rrrr-mm-dd'), ?, SYSDATE, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setString(2, start_date);
			ps.setString(3, end_date);
			ps.setString(4, text);
			ps.setInt(5, num);
			rs = ps.executeQuery();

			/**** 장소 등록 ****/
			sql = "INSERT INTO post_locations VALUES(?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setString(2, name);
			ps.setString(3, country);
			ps.setString(4, city);
			rs = ps.executeQuery();

			/**** 사진 등록 ****/
			if (picture != null) {
				String[] pictokens = picture.split(" ");
				int picnum = 0;

				for (int i = 0; i < pictokens.length; i++) {
					// picnum 지정
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

			/**** 태그 등록 ****/
			if (tag != null) {
				String[] tagtokens = tag.split(" #");
				int tagnum = 0;

				for (int i = 0; i < tagtokens.length; i++) {
					// tagnum 지정
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

	// 신고
	public void reportPost(int pnum, String reason) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rep_num = 0;

		System.out.println("포스트 신고");

		try {
			// 신고 번호 지정
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

			// 포스트 신고
			sql = "INSERT INTO record VALUES(?, 0, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setInt(2, rep_num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			System.out.println("신고가 완료되었습니다.");
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

		System.out.println("댓글 신고");

		try {
			// 신고 번호 지정
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

			// 포스트 신고
			sql = "INSERT INTO record VALUES(0, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, rnum);
			ps.setInt(2, rep_num);
			rs = ps.executeQuery();

			rs.close();
			ps.close();
			System.out.println("신고가 완료되었습니다.");
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	// 댓글 작성
	public void replyToPost(int pnum) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String reply = null;
		int rep_num = 0;

		System.out.println("댓글 작성 (취소 : 0) ");

		try {
			// rep_num 지정
			sql = "SELECT MAX(reply_num) FROM reply";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				rep_num = rs.getInt(1) + 1;
			}

			while (reply == null || reply.isEmpty()) {
				System.out.println("댓글을 입력하세요.");
				reply = sc.nextLine();
				if (reply.equals("0")) {
					break;
				}
				if (reply == null || reply.isEmpty())
					System.out.println("댓글이 공란입니다.");
			}
			if (reply.equals("0")) {
				System.out.println("취소되었습니다.");
			} else {
				sql = "INSERT INTO reply VALUES(?, ?, SYSDATE, ?, null, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rep_num);
				ps.setString(2, reply);
				ps.setInt(3, num);
				ps.setInt(4, pnum);
				rs = ps.executeQuery();

				System.out.println("댓글을 작성하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 대댓글 작성
		public void replyToReply(int pnum, int parentNum) {
			Database db = new Database();
			conn = db.getConnection();
			stmt = db.getStatement();
			String sql = null;
			PreparedStatement ps = null;
			ResultSet rs = null;

			String reply = null;
			int rep_num = 0;

			System.out.println("댓글 작성 (취소 : 0) ");

			try {
				// rep_num 지정
				sql = "SELECT MAX(reply_num) FROM reply";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					rep_num = rs.getInt(1) + 1;
				}

				while (reply == null || reply.isEmpty()) {
					System.out.println("댓글을 입력하세요.");
					reply = sc.nextLine();
					if (reply.equals("0")) {
						break;
					}
					if (reply == null || reply.isEmpty())
						System.out.println("댓글이 공란입니다.");
				}
				if (reply.equals("0")) {
					System.out.println("취소되었습니다.");
				} else {
					sql = "INSERT INTO reply VALUES(?, ?, SYSDATE, ?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, rep_num);
					ps.setString(2, reply);
					ps.setInt(3, num);
					ps.setInt(4, parentNum);
					ps.setInt(5, pnum);
					rs = ps.executeQuery();

					System.out.println("댓글을 작성하였습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	// 평가
	public void rating(int pnum) {
		Database db = new Database();
		conn = db.getConnection();
		stmt = db.getStatement();
		String sql = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int rate = 0;
		boolean isUploaded = false;

		System.out.println("별점 작성");

		try {
			sql = "SELECT * FROM rating WHERE post_num = ? AND traveler_num = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, pnum);
			ps.setInt(2, num);
			rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("이미 별점을 작성하였습니다.");
			} else {
				while (!isUploaded) {
					System.out.println("별점을 입력하세요.(1, 2, 3, 4, 5), (취소: 0)");
					rate = sc.nextInt();
					sc.nextLine();

					switch (rate) {
					case 0:
						System.out.println("취소되었습니다.");
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
						System.out.println("별점이 반영되었습니다.");
						isUploaded = true;
						break;
					default:
						System.out.println("잘못된 값입니다.");
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}