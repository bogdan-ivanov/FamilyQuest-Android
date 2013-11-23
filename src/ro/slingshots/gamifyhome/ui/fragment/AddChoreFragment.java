package ro.slingshots.gamifyhome.ui.fragment;

import ro.slingshots.gamifyhome.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddChoreFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.xp, container,
			false);
		
		return v;
	}
	
	@Override
	public void onResume() {
		
		
		super.onResume();
	}
}
