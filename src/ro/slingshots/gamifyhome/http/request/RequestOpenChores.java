package ro.slingshots.gamifyhome.http.request;

import java.io.IOException;

import ro.slingshots.gamifyhome.http.RestClient;
import ro.slingshots.gamifyhome.http.exception.HttpApiException;

public class RequestOpenChores extends GenericRequest<String> {

	@Override
	public String execute() throws HttpApiException, IOException {
		
		return RestClient.getInstance().getOpenChores();
	}

}
