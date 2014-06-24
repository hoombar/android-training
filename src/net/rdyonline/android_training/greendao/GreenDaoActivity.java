package net.rdyonline.android_training.greendao;

import net.rdyonline.android_training.R;
import net.rdyonline.android_training.greendao.data.ConferencePopulator;
import net.rdyonline.android_training.greendao.data.Populator;
import net.rdyonline.android_training.greendao.data.RoomPopulator;
import net.rdyonline.android_training.greendao.data.SpeakerPopulator;
import net.rdyonline.android_training.greendao.data.TimeslotPopulator;
import net.rdyonline.android_training.orm.dao.DaoSession;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GreenDaoActivity extends Activity {

	private Button mClear;
	private Button mPopulate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greendao);
		
		bindViews();
		setListeners();
	}
	
	private void bindViews() {
		mPopulate = (Button) findViewById(R.id.button_add_data);
		mClear = (Button) findViewById(R.id.button_clear_data);
	}
	
	private void setListeners() {
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_add_data:
					populateData(); 
					break;
				case R.id.button_clear_data:
					clearData(); 
					break;
				}
			}
		};
		
		mPopulate.setOnClickListener(listener);
		mClear.setOnClickListener(listener);
	}
	
	/***
	 * Some example data for the tutorial
	 */
	private void populateData() {
		populate(new ConferencePopulator());
		populate(new RoomPopulator());
		populate(new SpeakerPopulator());
		populate(new TimeslotPopulator());
	}
	
	private void populate(Populator p) {
		p.deleteData();
		p.populateData();
	}
	
	private void clearData() {
		DaoSession session = DbHelper.getInstance().getDaoSession();
		session.getConferenceDao().deleteAll();
		session.getRoomDao().deleteAll();
		session.getSpeakerDao().deleteAll();
		session.getTimeslotDao().deleteAll();
	}
}
