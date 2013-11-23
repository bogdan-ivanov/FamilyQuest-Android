package ro.slingshots.gamifyhome.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.http.request.AsyncCallback;
import ro.slingshots.gamifyhome.http.request.AsyncRequest;
import ro.slingshots.gamifyhome.http.request.RequestOpenChores;
import ro.slingshots.gamifyhome.http.response.ResponseOpenChores;
import ro.slingshots.gamifyhome.restapi.pojo.Chore;
import ro.slingshots.gamifyhome.ui.IOnFinish;
import ro.slingshots.gamifyhome.ui.ListItemViewHolder;
import ro.slingshots.gamifyhome.ui.fragment.BaseListFragment.MyAdapter;
import ro.slingshots.gamifyhome.ui.fragment.ChoreListFragment.ChoreAdapter;

public class ApproveListFragment extends BaseListFragment {
	protected static final String TAG = "RewardListFragment";

	public ApproveListFragment(){
		super(R.layout.reward_list);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = super.onCreateView(inflater, container, savedInstanceState);
		initAdapter();
		return v;
	}
	@Override
	public void initAdapter() {
		mAdapter = new RewardAdapter(getActivity().getLayoutInflater());
		mList.setAdapter(mAdapter);
	}
	public static void getVotingChores(final IOnFinish<List> finish){
		Log.i(TAG,"getVotingChores");
		new AsyncRequest(new AsyncCallback<String>() {
			
			@Override
			public void onFinish(String response, Exception e) {
				//Log.i(TAG,"onFinish() response " +response);
				ResponseOpenChores resp = ResponseOpenChores.fromString(response);

				if(finish!=null && resp!=null)
					finish.onFinish(filterVotingChores(resp.available_chores));
				
			}
		}).execute(new RequestOpenChores());
	}
	public static List filterVotingChores(List chores){
		if(chores==null || (chores!=null && chores.size()==0))
			return null;
		
		List result = new ArrayList<Chore>();
		for(int i=0;i<chores.size();i++){
			Chore c = (Chore)chores.get(i);
			if(c.voting)
				result.add(c);
		}
		return result;
	}
	@Override
	public void onResume() {
		getVotingChores(new IOnFinish<List>() {

			@Override
			public void onFinish(List t) {
				setObjectList(t);
			}
			
		});
		super.onResume();
	}
	class RewardAdapter extends MyAdapter{
		
		public RewardAdapter(LayoutInflater li) {
			super(li);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItemViewHolder holder=null;
			if(convertView==null){
				convertView = layoutInflater.inflate(R.layout.reward_item, null);
				holder = new ListItemViewHolder();
				holder.description = (TextView)convertView.findViewById(R.id.description);
				//TODO add picture url
				convertView.setTag(holder);
			}else{
				holder = (ListItemViewHolder)convertView.getTag();
			}
			
			holder.mapReward(convertView,(Chore)getItem(position));
			return convertView;
		}
		
	}
}