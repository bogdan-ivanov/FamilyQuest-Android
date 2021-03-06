package ro.slingshots.gamifyhome.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;

import ro.slingshots.gamifyhome.http.exception.HttpApiException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


public class RestClient {
private static final String TAG = "RestClient";
	//public static final String URL_BASE = "http://172.28.101.30:8080";
	public static final String URL_BASE = "http://192.168.1.3:8080";
	public static final String PATH_OPEN_CHORES = "/api/v1/family/1/?format=json";
	public static final String PATH_CLOSE_CHORE = "/photo_upload/";

	public static final String PATH_LOGIN = "/request_api_key/";

	public static final String PATH_ADD_CHORE = "/api/v1/chore/";
	public static final String PATH_MEMBER = "/api/v1/family_member/";

	private HttpApi mHttpApi;
	private static RestClient mInstance;
	private RestClient(){
		mHttpApi = new HttpApi("1");
	}
	public String getUrl(String path){
		return URL_BASE+path;
	}
	public String getOpenChores() throws HttpApiException, IOException{
		return execute(getUrl(PATH_OPEN_CHORES));
	}
	public String getEncodedImage(String imagePath){
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
		
		return encodedImage;
	}
	public String submitChore(String json)throws HttpApiException, IOException{
		
		String url = getUrl(PATH_ADD_CHORE);
		Log.i(TAG,"submitChore "+url);
		HttpPost post = new HttpPost(url);
		
		return execute(post,json);
	}
	public String uploadClosedChore(String imagePath,String json) throws HttpApiException, IOException{
		
		String url = getUrl(PATH_CLOSE_CHORE);
		Log.i(TAG,"uploadClosedChore "+url);
		HttpPost post = new HttpPost(url);
		
        return execute(post,imagePath,json);
	}
	public String login(String json) throws HttpApiException, IOException{
		String url = getUrl(PATH_LOGIN);
		HttpPost post = new HttpPost(url);
		
		return execute(post,json);
	}
	public String execute(String url) throws HttpApiException, IOException{
		Log.i(TAG,"GET "+url);
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Content-Type", "application/json");
		return mHttpApi.executeHttpRequest(get);
	}
	public String execute(HttpPost post,String imagePath,String jsonRequest) throws HttpApiException, IOException{
		Log.i(TAG,"BODY "+jsonRequest);
		Log.i(TAG,"imagePath "+imagePath);
		
		String encodedImage = getEncodedImage(imagePath);
		Log.i(TAG,"encodedSize "+encodedImage.length());
		if(encodedImage!=null){
			 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			 nameValuePairs.add(new BasicNameValuePair("image",encodedImage));
			 post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		post.setHeader("Content-Type", "application/octet-stream");
		
		return mHttpApi.executeHttpRequest(post);
	}
	
	public String execute(HttpPost post,String jsonRequest) throws HttpApiException, IOException{
		Log.i(TAG,"BODY "+jsonRequest);
		if(jsonRequest!=null)
			post.setEntity(new ByteArrayEntity(jsonRequest.getBytes("UTF8")));
		post.setHeader("Content-Type", "application/json");
		return mHttpApi.executeHttpRequest(post);
	}
	public static RestClient getInstance(){
		if(mInstance==null)
			mInstance = new RestClient();
		
		return mInstance;
	}
	
	public static String getInitiator(int id){
		return PATH_MEMBER+id+"/";
	}
}
