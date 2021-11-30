package post;

import java.sql.Date;

public class Post {
	private int num;
	private Date startDate;
	private Date endDate;
	private String text;
	private Date writtenTime;
	private int travelerNum;
	private int viewCnt;
	private int bookmarkCnt;

	public Post(int num, int viewCnt, int bookmarkCnt) {
		super();
		this.num = num;
		this.viewCnt = viewCnt;
		this.bookmarkCnt = bookmarkCnt;
	}

	public Post(int num, Date startDate, Date endDate, String text, Date writtenTime, int travelerNum, int viewCnt,
			int bookmarkCnt) {
		super();
		this.num = num;
		this.startDate = startDate;
		this.endDate = endDate;
		this.text = text;
		this.writtenTime = writtenTime;
		this.travelerNum = travelerNum;
		this.viewCnt = viewCnt;
		this.bookmarkCnt = bookmarkCnt;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public int getBookmarkCnt() {
		return bookmarkCnt;
	}

	public void setBookmarkCnt(int bookmarkCnt) {
		this.bookmarkCnt = bookmarkCnt;
	}
}
