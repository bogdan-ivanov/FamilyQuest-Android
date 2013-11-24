package ro.slingshots.gamifyhome.ui.fragment;

import ro.slingshots.gamifyhome.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RewardFragment extends BaseFragment implements OnItemSelectedListener{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.xp, container,
			false);
		Spinner spinner = (Spinner)v.findViewById(R.id.spinner_rewards);
		spinner.setOnItemSelectedListener(this);
		return v;
	}
	
	@Override
	public void onResume() {
		//load data and populate spinner
		
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
