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
import net.rdyonline.android_training.orm.dao.ConferenceDao;
import net.rdyonline.android_training.orm.dao.DaoSession;
import net.rdyonline.android_training.orm.dao.RoomDao;
import net.rdyonline.android_training.orm.dao.SpeakerDao;
import net.rdyonline.android_training.orm.dao.TimeslotDao;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/***
 * Most of the interesting stuff for the training session is in here
 * 
 * @author rdy
 */
public class GreenDaoActivity extends Activity {

	private static final String TAG = GreenDaoActivity.class.getSimpleName();

	private Button mClear;
	private Button mPopulate;
	private Button mRunInTx;
	private Button mRunNoTx;
	private Button mCaching;
	private Button mCachingProblem;
	private TextView mOutput;

	// how many times to iterate time slots when demonstrating transaction speed
	private int mTxIterations = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greendao);

		bindViews();
		setListeners();

		// the transaction data is too big to show, so it gets cleared on start
		clearData();
		updateOutput();
	}

	private void bindViews() {
		mPopulate = (Button) findViewById(R.id.button_add_data);
		mClear = (Button) findViewById(R.id.button_clear_data);
		mRunInTx = (Button) findViewById(R.id.button_run_in_tx);
		mRunNoTx = (Button) findViewById(R.id.button_run_without_tx);
		mCaching = (Button) findViewById(R.id.button_caching);
		mCachingProblem = (Button) findViewById(R.id.button_caching_problem);
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
				case R.id.button_run_in_tx:
					runInTx();
					break;
				case R.id.button_run_without_tx:
					runWithoutTx();
					break;
				case R.id.button_caching:
					cachingDemo();
					break;
				case R.id.button_caching_problem:
					cachingProblemExample();
					break;
				}
			}
		};

		mPopulate.setOnClickListener(listener);
		mClear.setOnClickListener(listener);
		mRunInTx.setOnClickListener(listener);
		mRunNoTx.setOnClickListener(listener);
		mCaching.setOnClickListener(listener);
		mCachingProblem.setOnClickListener(listener);
	}

	/**
	 * Illustrate how GreenDao handles caching The same object is returned from
	 * a new query, so if you update an object returned from a previous query,
	 * you will also be updating the newer object that was returned from a more
	 * recent query
	 */
	private void cachingDemo() {
		// start with a clean slate
		populateData();

		DaoSession session = DbHelper.getInstance().getDaoSession();
		SpeakerDao speakerDao = session.getSpeakerDao();
		TimeslotDao timeDao = session.getTimeslotDao();

		// use a speaker and a timeslot for the demo
		Speaker speaker = speakerDao.loadAll().get(0);
		Timeslot timeslot = speaker.getTimeslotList().get(0);

		Log.d(TAG,
				"Timeslot value before update is: "
						+ Long.toString(timeslot.getRoom()));

		Timeslot directTimeslot = timeDao.load(timeslot.getId());
		directTimeslot.setRoom(-1L);
		timeDao.update(directTimeslot);

		// note that timeslot hasn't been explicitly updated!

		Log.d(TAG,
				"Direct Timeslot for speaker is: "
						+ Long.toString(directTimeslot.getRoom()));
		Log.d(TAG,
				"GreenDao returns the same Java object, so timeslot is also: "
						+ Long.toString(timeslot.getRoom()));

		Toast.makeText(this, "Check logcat", Toast.LENGTH_SHORT).show();

		clearData();
	}

	/***
	 * As GreenDao works with cached versions of objects, inserting a new record
	 * has the potential to make any other objects that use a relationship to
	 * access child objects stale
	 */
	private void cachingProblemExample() {
		// need data to work with
		populateData();

		DaoSession session = DbHelper.getInstance().getDaoSession();
		ConferenceDao conferenceDao = session.getConferenceDao();
		RoomDao roomDao = session.getRoomDao();
		Conference conference = conferenceDao.loadAll().get(0);

		Room room = new Room();
		room.setCapacity(100L);
		room.setConference(conference.getId());
		room.setName("MissingFromConf");

		// added to database
		roomDao.insert(room);

		// loadAll will hit the database again to get the most recent
		// list of all of the rooms that are available again
		List<Room> dbRooms = roomDao.loadAll();
		int roomCount = 0;
		for (int i = 0; i < dbRooms.size(); i++) {
			if (dbRooms.get(i).getConference() == conference.getId()) {
				roomCount++;
			}
		}

		Log.d(TAG, "Rooms in database: " + roomCount);
		Log.d(TAG, "Rooms conference has: " + conference.getRoomList().size());
		Toast.makeText(this, "Check logcat", Toast.LENGTH_SHORT).show();

		clearData();
	}

	/***
	 * 1000 rows added, but by running them in a transaction, notice the speed
	 * difference
	 * 
	 * A single transaction will be opened for all of the inserted rather than:
	 * open transaction insert close transaction open transaction insert ..
	 */
	private void runInTx() {
		// make sure dependencies are met
		populateData();

		final TimeslotPopulator tp = new TimeslotPopulator();

		DbHelper.getInstance().getDaoSession().runInTx(new Runnable() {

			@Override
			public void run() {
				tp.populateData(mTxIterations);
			}
		});

		Toast.makeText(this, "Done,  pull database to see data",
				Toast.LENGTH_LONG).show();
	}

	/***
	 * 1000 rows added one by one
	 * 
	 * Note that a new transaction will be created for each record inserted
	 */
	private void runWithoutTx() {
		// make sure dependencies are met
		populateData();

		final TimeslotPopulator tp = new TimeslotPopulator();

		tp.deleteData();
		tp.populateData(mTxIterations);

		Toast.makeText(this, "Done,  pull database to see data",
				Toast.LENGTH_LONG).show();
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

	/***
	 * Clean all data that has been inserted
	 */
	private void clearData() {
		DaoSession session = DbHelper.getInstance().getDaoSession();
		session.getConferenceDao().deleteAll();
		session.getRoomDao().deleteAll();
		session.getSpeakerDao().deleteAll();
		session.getTimeslotDao().deleteAll();

		updateOutput();
	}

	/***
	 * Used to display the current state of the database
	 * 
	 * Note that this won't be used for the transaction demonstrations
	 * as the data would be too large
	 */
	private void updateOutput() {
		DaoSession session = DbHelper.getInstance().getDaoSession();
		List<Conference> conferences = session.getConferenceDao().loadAll();
		List<Speaker> speakers = session.getSpeakerDao().loadAll();

		StringBuilder builder = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

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
								builder.append(s.getFname() + " ");
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
