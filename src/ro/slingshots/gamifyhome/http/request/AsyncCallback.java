package ro.slingshots.gamifyhome.http.request;

public interface AsyncCallback<R>{
		public void onFinish(R response,Exception e);
}