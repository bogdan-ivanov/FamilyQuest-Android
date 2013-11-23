package ro.slingshots.gamifyhome.http.response;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ro.slingshots.gamifyhome.restapi.pojo.Chore;

public class ResponseOpenChores {
	
	public List<Chore> available_chores;
	public String[] members;
	public String name;
	public String resource_uri;
	
	public static ResponseOpenChores fromString(String s){
		Gson gson = new GsonBuilder().create();
		ResponseOpenChores fromJson = gson.fromJson(s,ResponseOpenChores.class);
		return fromJson;
	}
}
