package ro.slingshots.gamifyhome.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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

public class ChoreListFragment extends BaseListFragment {
	
	
	
	protected static final String TAG = "ChoreListFragment";


	public ChoreListFragment(){
		super(R.layout.chore_list);
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
		mAdapter = new ChoreAdapter(getActivity().getLayoutInflater());

		
		mList.setAdapter(mAdapter);
	}
	
	
	@Override
	public void onResume() {
		new AsyncRequest(new AsyncCallback<String>() {
			
			@Override
			public void onFinish(String response, Exception e) {
				//Log.i(TAG,"onFinish() response " +response);
				ResponseOpenChores resp = ResponseOpenChores.fromString(response);
				//Log.i(TAG,"first "+resp.available_chores.get(0).text);
				setObjectList(resp.available_chores);
			}
		}).execute(new RequestOpenChores());
		super.onResume();
	}


	class ChoreAdapter extends MyAdapter{
		
		public ChoreAdapter(LayoutInflater li) {
			super(li);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItemViewHolder holder=null;
			if(convertView==null){
				convertView = layoutInflater.inflate(R.layout.chore_item, null);
				holder = new ListItemViewHolder();
				holder.description = (TextView)convertView.findViewById(R.id.description);
				holder.time = (TextView)convertView.findViewById(R.id.date);
				holder.xp = (TextView)convertView.findViewById(R.id.xp);
				convertView.setTag(holder);
			}else{
				holder = (ListItemViewHolder)convertView.getTag();
			}
			
			holder.mapChore(convertView,(Chore)getItem(position));
			return convertView;
		}
		
	}
	
}
