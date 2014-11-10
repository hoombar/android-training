package net.rdyonline.android_training.alarms;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlarmActivity extends Activity {

	private static final String TAG = AlarmActivity.class.getSimpleName();

	private Context mContext;
	private WrappedAlarmManager mAlarmManager;

	private Button mBtnSingle;
	private Button mBtnRepeating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarms);

		mContext = this;
		mAlarmManager = new WrappedAlarmManager(mContext);

		bindViews();
		setListeners();
	}

	private void bindViews() {
		mBtnSingle = (Button) findViewById(R.id.button_single_alarm);
		mBtnRepeating = (Button) findViewById(R.id.button_repeating_alarm);
	}

	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_single_alarm:
					mAlarmManager.scheduleSingleAlarm(AlarmActivity.this);
					break;
				case R.id.button_repeating_alarm:
					mAlarmManager.scheduleRepeatingAlarm(AlarmActivity.this);
					break;
				}
			}
		};

		mBtnSingle.setOnClickListener(listener);
		mBtnRepeating.setOnClickListener(listener);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mAlarmManager.removeRepeatingAlarm(this);
	}

}
