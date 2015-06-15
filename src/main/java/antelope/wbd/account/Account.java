package antelope.wbd.account;

public class Account {

	public static final String USER_ID = "userId";
	private String userId;

	public Account(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
