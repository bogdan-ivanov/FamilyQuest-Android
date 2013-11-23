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
	public static final String URL_BASE = "http://172.28.101.30:8080";
	public static final String PATH_OPEN_CHORES = "/api/v1/family/1/?format=json";
	public static final String PATH_CLOSE_CHORE = "/photo_upload/";
	private HttpApi mHttpApi;
	private static RestClient mInstance;
	private RestClient(){
		mHttpApi = new HttpApi("1");
	}
	public String getUrl(String path){
		return URL_BASE+path;
	}
	public String getOpenChores() throws HttpApiException, IOException{
		return execute(getUrl(PATH_OPEN_CHORES),new HttpGet());
	}
	public String getEncodedImage(String imagePath){
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
		
		return encodedImage;
	}
	public String uploadClosedChore(String imagePath,String json) throws HttpApiException, IOException{
		HttpPost post = new HttpPost();
		
        return execute(getUrl(PATH_CLOSE_CHORE),post,imagePath,json);
	}
	public String execute(String url,HttpGet get) throws HttpApiException, IOException{
		Log.i(TAG,"GET "+url);
		get = new HttpGet(url);
		
		get.setHeader("Content-Type", "application/json");
		return mHttpApi.executeHttpRequest(get);
	}
	public String execute(String url,HttpPost post,String imagePath,String jsonRequest) throws HttpApiException, IOException{
		Log.i(TAG,"POST "+url);
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
	
	public String execute(String url,HttpPost post,String jsonRequest) throws HttpApiException, IOException{
		Log.i(TAG,"POST "+url);
		Log.i(TAG,"BODY "+jsonRequest);
		post = new HttpPost(url);
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
}
