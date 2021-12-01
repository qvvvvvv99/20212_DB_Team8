package post;

public class Report {
	private int report_num;
	private String type;
	private int pr_num; //포스트 혹은 댓글 넘버
	private String reason;
	private int traveler_num;
	
	public Report() {
		
	}
	
	public Report(int report_num, String type, String reason, int traveler_num) {
		this.report_num = report_num;
		this.type = type;
		this.reason = reason;
		this.traveler_num = traveler_num;
	}
	
	public Report(int report_num, String type, String reason, int pr_num, int traveler_num) {
		this.report_num = report_num;
		this.type = type;
		this.pr_num = pr_num;
		this.reason = reason;
		this.traveler_num = traveler_num;
	}
	
	public int getPr_num() {
		return pr_num;
	}
	
	public void setPr_num(int pr_num) {
		this.pr_num = pr_num;
	}
	
	public int getReport_num() {
		return report_num;
	}
	public void setReport_num(int report_num) {
		this.report_num = report_num;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getTraveler_num() {
		return traveler_num;
	}
	public void setTraveler_num(int traveler_num) {
		this.traveler_num = traveler_num;
	}
	
}
