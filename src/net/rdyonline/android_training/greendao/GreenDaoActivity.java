package net.rdyonline.android_training.greendao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import net.rdyonline.android_training.R;
import net.rdyonline.android_training.greendao.data.ConferencePopulator;
import net.rdyonline.android_training.greendao.data.Populator;
import net.rdyonline.android_training.greendao.data.RoomPopulator;
import net.rdyonline.android_training.greendao.data.SpeakerPopulator;
import net.rdyonline.android_training.greendao.data.TimeslotPopulator;
import net.rdyonline.android_training.orm.Conference;
import net.rdyonline.android_training.orm.Room;
import net.rdyonline.android_training.orm.Speaker;
import net.rdyonline.android_training.orm.Timeslot;
import net.rdyonline.android_training.orm.dao.DaoSession;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GreenDaoActivity extends Activity {

	private Button mClear;
	private Button mPopulate;
	private TextView mOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greendao);

		bindViews();
		setListeners();
		
		updateOutput();
	}

	private void bindViews() {
		mPopulate = (Button) findViewById(R.id.button_add_data);
		mClear = (Button) findViewById(R.id.button_clear_data);
		mOutput = (TextView) findViewById(R.id.txt_dao_output);
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

		updateOutput();
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

		updateOutput();
	}

	private void updateOutput() {
		DaoSession session = DbHelper.getInstance().getDaoSession();
		List<Conference> conferences = session.getConferenceDao().loadAll();
		List<Speaker> speakers = session.getSpeakerDao().loadAll();

		StringBuilder builder = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
		
		// TODO(benp) good chance for to demonstrate custom query
		for (Conference c : conferences) {
			builder.append(c.getName() + "\n");
			
			for (Room r : c.getRoomList()) {
				builder.append("\t" + r.getName() + "\n");
				
				for (Timeslot t : r.getTimeslotList()) {
					builder.append("\t\t");
					builder.append(sdf.format(t.getStartTime()));
					builder.append(" - ");
					builder.append(sdf.format(t.getEndTime()));
					
					for (Speaker s : speakers) {
						for (Timeslot st : s.getTimeslotList()) {
							if (st.getId() == t.getId()) {
								builder.append(" (");
								builder.append(s.getFname()+" ");
								builder.append(s.getLname());
								builder.append(")");
								break;
							}
						}
					}
					
					builder.append("\n");
				}
			}
		}

		mOutput.setText(builder.toString());
	}
}
