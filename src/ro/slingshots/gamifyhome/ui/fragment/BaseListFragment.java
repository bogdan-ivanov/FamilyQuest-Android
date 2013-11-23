package ro.slingshots.gamifyhome.ui.fragment;

import java.util.ArrayList;
import java.util.List;


import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.ui.IOnFinish;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BaseListFragment extends BaseFragment {
	ListView mList;
	MyAdapter mAdapter;
	protected IOnFinish<Integer> onClick;
	int mRootLayoutId;
	
	public BaseListFragment(){
		mRootLayoutId = R.layout.chore_list;
	}
	public BaseListFragment(int layoutResId) {
		mRootLayoutId = layoutResId;
	}
	public void registerOnListItemClick(IOnFinish<Integer> onFinish){
		onClick = onFinish;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View v = inflater.inflate(mRootLayoutId, container,
				false);
		
		mList = (ListView)v.findViewById(R.id.listview);
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(onClick!=null){
					onClick.onFinish(arg2);
				}
			}
			
		});
		return v;
	}
	public void initAdapter(){
		mAdapter = new MyAdapter(getActivity().getLayoutInflater());
		//test
		ArrayList<Object> testList = new ArrayList<Object>();
		for(int i=0;i<1000;i++)
			testList.add(new Integer(1));
		
		setObjectList(testList);
		//end test
		
		mList.setAdapter(mAdapter);
	}
	public void setObjectList(final List list) {
		if(list==null)
			return;
		
		mList.post(new Runnable() {

			@Override
			public void run() {
				mAdapter.setObjects(list);
			}
		});
	}
	public class MyAdapter extends BaseAdapter{
		List mObjects;
		LayoutInflater layoutInflater;
		
		public MyAdapter(LayoutInflater li){
			layoutInflater = li;
		}
		public void setObjects(List list){
			mObjects = list;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			
			return mObjects!=null? mObjects.size():0;
		}

		@Override
		public Object getItem(int position) {
			
			return mObjects!=null?  mObjects.get(position):null;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				convertView = layoutInflater.inflate(R.layout.chore_item, null);
			}else{
				//map from viewholder
			}
			return convertView;
		}
		
		
		
	}

}
