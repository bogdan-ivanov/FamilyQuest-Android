package ro.slingshots.gamifyhome.http.request;

import java.io.IOException;

import ro.slingshots.gamifyhome.http.RestClient;
import ro.slingshots.gamifyhome.http.exception.HttpApiException;

public class RequestLogin extends GenericRequest<String> {
	String mJson;
	public RequestLogin(String json){
		mJson = json;
	}
	
	@Override
	public String execute() throws HttpApiException, IOException {
		
		return RestClient.getInstance().login(mJson);
	}
}
