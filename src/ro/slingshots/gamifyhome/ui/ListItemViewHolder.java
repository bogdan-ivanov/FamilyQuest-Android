package ro.slingshots.gamifyhome.ui;

import ro.slingshots.gamifyhome.restapi.pojo.Chore;
import android.view.View;
import android.widget.TextView;

public class ListItemViewHolder {
	public TextView description;
	public TextView time;
	public TextView xp;
	//TODO add POJO PARAM
	public void mapChore(View v,Chore c){
		description.setText(c.text);
		time.setText(c.time);
		xp.setText("XP: "+c.xp_reward);
		
	}
	public void mapReward(View v,Chore c){
		description.setText(c.text);
		//TODO add pic loading here..
	}
}
