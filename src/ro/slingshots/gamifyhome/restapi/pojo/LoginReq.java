package ro.slingshots.gamifyhome.restapi.pojo;

public class LoginReq {
	public String username;
	public String password;
	
	public LoginReq(String user, String pass) {
		username = user;
		password = pass;
	}
}
