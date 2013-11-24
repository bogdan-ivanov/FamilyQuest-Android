package ro.slingshots.gamifyhome.http.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ResponseLogin {
	public String api_key;

	public static ResponseLogin fromString(String s){
		Gson gson = new GsonBuilder().create();
		ResponseLogin fromJson = gson.fromJson(s, ResponseLogin.class);
		return fromJson;
	}
}
