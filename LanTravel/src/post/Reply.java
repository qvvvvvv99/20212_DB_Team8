package post;

import java.sql.Date;

public class Reply {
	private int num;
	private String text;
	private Date writtenTime;
	private int travelerNum;
	private int parentNum;
	private int postNum;

	public Reply() {
		
	}

	public Reply(int num, String text, Date writtenTime, int travelerNum, int parentNum) {
		super();
		this.num = num;
		this.text = text;
		this.writtenTime = writtenTime;
		this.travelerNum = travelerNum;
		this.parentNum = parentNum;
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
}
