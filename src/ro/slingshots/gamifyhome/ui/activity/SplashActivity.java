package ro.slingshots.gamifyhome.ui.activity;


import org.json.JSONObject;

import com.google.gson.GsonBuilder;

import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.http.request.AsyncCallback;
import ro.slingshots.gamifyhome.http.request.AsyncRequest;
import ro.slingshots.gamifyhome.http.request.RequestLogin;
import ro.slingshots.gamifyhome.http.request.RequestOpenChores;
import ro.slingshots.gamifyhome.http.response.ResponseLogin;
import ro.slingshots.gamifyhome.http.response.ResponseOpenChores;
import ro.slingshots.gamifyhome.restapi.pojo.LoginReq;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SplashActivity extends Activity {
	public static final String TAG ="SplashActivity";

	EditText mUserInput;
	EditText mPassInput;
	Button mLoginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		mUserInput = (EditText) findViewById(R.id.username_input);
		mPassInput = (EditText) findViewById(R.id.password_input);
		mLoginButton = (Button) findViewById(R.id.login_button);
		
		mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final LoginReq login_data = new LoginReq(mUserInput.getText().toString(),
        				mPassInput.getText().toString());
            	
            	new AsyncRequest(new AsyncCallback<String>() {
        			
        			@Override
        			public void onFinish(String response, Exception e) {
        				ResponseLogin resp = ResponseLogin.fromString(response);
        				if (resp.api_key != null) {
        					// TODO: persist
        					Log.i(TAG,"onFinish() response API-KEY::" + resp.api_key);
        					Intent activityChangeIntent = new Intent(SplashActivity.this, MainActivity.class);
        	                SplashActivity.this.startActivity(activityChangeIntent);
        				} else {
        					// Username or password false, display and an error
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SplashActivity.this);

                            dlgAlert.setMessage("Wrong password or username");
                            dlgAlert.setTitle("Error");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();

                            dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                }
                            });
        				}
        			}
        		}).execute(new RequestLogin(new GsonBuilder().create().toJson(login_data)));

            }
        });
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

}
