package post;

import java.sql.Date;

public class Reply {
	private int num;
	private String text;
	private Date writtenTime;
	private int travelerNum;
	private String travelerNickname;
	private int parentNum;
	private int postNum;
	private int depth;

	public Reply() {

	}

	public Reply(int num, String text, Date writtenTime, int travelerNum, String travelerNickname, int parentNum) {
		super();
		this.num = num;
		this.text = text;
		this.writtenTime = writtenTime;
		this.travelerNum = travelerNum;
		this.travelerNickname = travelerNickname;
		this.parentNum = parentNum;
	}

	public Reply(int num, String text, Date writtenTime, int travelerNum, String travelerNickname, int parentNum,
			int depth) {
		super();
		this.num = num;
		this.text = text;
		this.writtenTime = writtenTime;
		this.travelerNum = travelerNum;
		this.travelerNickname = travelerNickname;
		this.parentNum = parentNum;
		this.depth = depth;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getWrittenTime() {
		return writtenTime;
	}

	public void setWrittenTime(Date writtenTime) {
		this.writtenTime = writtenTime;
	}

	public int getTravelerNum() {
		return travelerNum;
	}

	public void setTravelerNum(int travelerNum) {
		this.travelerNum = travelerNum;
	}

	public String getTravelerNickname() {
		return travelerNickname;
	}

	public void setTravelerNickname(String travelerNickname) {
		this.travelerNickname = travelerNickname;
	}

	public int getParentNum() {
		return parentNum;
	}

	public void setParentNum(int parentNum) {
		this.parentNum = parentNum;
	}

	public int getPostNum() {
		return postNum;
	}

	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
