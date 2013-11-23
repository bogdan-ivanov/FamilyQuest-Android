package ro.slingshots.gamifyhome.http.request;

import java.io.IOException;

import ro.slingshots.gamifyhome.http.RestClient;
import ro.slingshots.gamifyhome.http.exception.HttpApiException;

public class RequestCloseChore extends GenericRequest<String>{
	String mPath,mJson;
	public RequestCloseChore(String path,String json){
		mPath = path;
		mJson = json;
	}
	@Override
	public String execute() throws HttpApiException, IOException {
		
		return RestClient.getInstance().uploadClosedChore(mPath, mJson);
	}
}
