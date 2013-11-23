package ro.slingshots.gamifyhome.ui.activity;




import ro.slingshots.gamifyhome.R;
import ro.slingshots.gamifyhome.R.layout;
import ro.slingshots.gamifyhome.R.menu;
import ro.slingshots.gamifyhome.http.request.AsyncCallback;
import ro.slingshots.gamifyhome.http.request.AsyncRequest;
import ro.slingshots.gamifyhome.http.request.RequestOpenChores;
import ro.slingshots.gamifyhome.http.response.ResponseOpenChores;
import ro.slingshots.gamifyhome.ui.IOnFinish;
import ro.slingshots.gamifyhome.ui.fragment.BaseListFragment;
import ro.slingshots.gamifyhome.ui.fragment.ChoreListFragment;
import ro.slingshots.gamifyhome.ui.fragment.RewardListFragment;
import ro.slingshots.gamifyhome.ui.fragment.SnapShotFragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{
	public static final String TAG ="MainActivity";
	FragmentPageAdapter mFragmentAdapter;
	android.support.v4.view.ViewPager mViewPager;
	Fragment mActiveFragment;
	IOnFinish<Integer> onChoreClick = new IOnFinish<Integer>() {

		@Override
		public void onFinish(Integer t) {
			Log.i(TAG,"onCHore click "+t);
			toSnapshotFragment();
		}
	
	};
	IOnFinish<Integer> onRewardClick = new IOnFinish<Integer>() {

		@Override
		public void onFinish(Integer t) {
			Log.i(TAG,"onReward click "+t);
		}
	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mFragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mFragmentAdapter);
		
		mViewPager.setOnPageChangeListener(this);
		PagerTitleStrip pagerTabStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		// @RTR API Level higher `than 10
		// pagerTabStrip.setActivated(false);		
		pagerTabStrip.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		//test chores REST API
		
		
		
		super.onResume();
	}

	public void toSnapshotFragment(){
		addFragment(getSupportFragmentManager().beginTransaction(), new SnapShotFragment());
	}
	public void addFragment(FragmentTransaction transaction,Fragment fragment){
		mActiveFragment = fragment;
		transaction.add(R.id.main, fragment);
		transaction.commit();
	}
	
	public void removeFragment(FragmentTransaction transaction,Fragment fragment){
		transaction.remove(fragment);
		transaction.commit();
		mActiveFragment = null;
	}
	@Override
	public void onBackPressed() {
		if(mActiveFragment!=null){
			removeFragment(getSupportFragmentManager().beginTransaction(), mActiveFragment);
		}else
			super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		//handle the badge button:
		menu.findItem(R.id.action_view_pending)
			.getActionView()
			.findViewById(R.id.button_view_pending)
			.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"onCLick action view pending");
				goToTab(2);
			}
		});
		
		return true;
	}
	
	public void goToTab(int tabPos){
		final int pos = tabPos;
		mViewPager.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mViewPager.setCurrentItem(pos);
			}
		},100);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_add_chore:{
			Log.i(TAG,"show new chore");
			break;
		}
		case R.id.action_view_pending:{
			Log.i(TAG,"go to pending tab");
			break;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}



	class FragmentPageAdapter extends FragmentStatePagerAdapter{
	
		
		public FragmentPageAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			BaseListFragment baseListFr = new BaseListFragment();
			switch(position){
				case 0:{
					baseListFr =  new ChoreListFragment();
					baseListFr.registerOnListItemClick(onChoreClick);
					break;
				}
				case 1:{
					baseListFr =  new RewardListFragment();
					baseListFr.registerOnListItemClick(onRewardClick);
					break;
				}
				case 2:{
					baseListFr =  new ChoreListFragment();
					baseListFr.registerOnListItemClick(onChoreClick);
					break;
				}
			}
			return baseListFr;
			
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3; 
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
