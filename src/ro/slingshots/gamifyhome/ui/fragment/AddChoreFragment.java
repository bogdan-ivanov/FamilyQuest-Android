package ro.slingshots.gamifyhome.ui.fragment;

import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.http.RestClient;
import ro.slingshots.gamifyhome.http.request.AsyncCallback;
import ro.slingshots.gamifyhome.http.request.AsyncRequest;
import ro.slingshots.gamifyhome.http.request.RequestAddChore;
import ro.slingshots.gamifyhome.pref.PrefValues;
import ro.slingshots.gamifyhome.restapi.pojo.ChoreAddData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddChoreFragment extends BaseFragment {
	protected static final String TAG = "AddChoreFragment";
	EditText mDescription;
	SeekBar mSeek;
	TextView mXpText;
	Button mSubmit;
	int mXpLevel = 5;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.add_chore, container,
			false);
		mDescription = (EditText)v.findViewById(R.id.edit_description);
		mSeek = (SeekBar)v.findViewById(R.id.seek_xp);
		mXpText = (TextView)v.findViewById(R.id.text_xp_value);
		mSubmit = (Button)v.findViewById(R.id.button_submit);
		
		mSeek.setMax(10);
		mSeek.setProgress(mXpLevel);
		updateXP(mXpLevel);
		mSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progress<0)
					progress =0;
				if(progress>10)
					progress=10;
				updateXP(progress);
				
			}
		});
		
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});
		return v;
	}
	public void updateXP(final int level){
	
		
		mXpText.post(new Runnable() {
			
			@Override
			public void run() {
				mXpText.setText(mXpText.getResources().getString(R.string.text_xp_value,getRealXpValue(level)));
				mXpLevel = level;
			}
		});
	}
	public int getRealXpValue(int level){
		return level*50;
	}
	public void onSubmit(){
		//TODO put good id
		ChoreAddData data = new ChoreAddData(RestClient.getInitiator(2), PrefValues.getAllowedMembers(getActivity()));
		data.text = mDescription.getText().toString();
		data.xp_reward = getRealXpValue(mXpLevel);
		new AsyncRequest(new AsyncCallback<String>() {
			
			@Override
			public void onFinish(String response, Exception e) {
				if(e==null){
					//ok to go
					//AddChoreFragment.this.getActivity().onBackPressed();
					Log.i(TAG,"Add Chore Resp" + response);
				}else{
					Toast.makeText(AddChoreFragment.this.getActivity(), "error submitting ", Toast.LENGTH_SHORT).show();
				}
			}
		}).execute(new RequestAddChore(data));
	}
	@Override
	public void onResume() {
		
		
		super.onResume();
	}
}
