package ro.slingshots.gamifyhome.http.request;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.JsonSyntaxException;


import ro.slingshots.gamifyhome.http.exception.HttpApiException;
import ro.slingshots.gamifyhome.ui.IOnFinish;
import android.os.AsyncTask;

	public class AsyncRequest extends AsyncTask<GenericRequest,Void,String>{
		AsyncCallback<String> mCallback;
		Exception mException = null;
		public AsyncRequest(AsyncCallback<String> finish){
			mCallback = finish;
			mException = null;
		}
		@Override
		protected String doInBackground(GenericRequest... params) {
			GenericRequest<String> request = params[0];
			String r = null;
			try{
				r = request.execute();
				
			}catch (HttpApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mException = e;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mException = e;
			}catch (IllegalStateException e){
				e.printStackTrace();
				mException = e;
			}catch (JsonSyntaxException e){
				e.printStackTrace();
				mException = e;
			}
			return r;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(mCallback!=null)
				mCallback.onFinish(result, mException);
		}
		
	}
	
