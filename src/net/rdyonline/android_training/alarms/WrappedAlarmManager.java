package net.rdyonline.android_training.alarms;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WrappedAlarmManager {

	private static final int SINGLE_ALARM_ID = 0x10101;
	private static final int REPEAT_ALARM_ID = 0x01010;
	private static final String KEY_SINGLE = "single_alarm";
	public static final String KEY_REPEAT = "repeat_alarm";

	private final long INTERVAL_SEVEN_SECONDS = 7 * 1000;

	private AlarmManager mAlarmManager;

	public WrappedAlarmManager(Context context) {
		mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void scheduleSingleAlarm(Context context) {
		Intent intent = new Intent(context, NotificationReceiver.class);
		Bundle extras = new Bundle();
		extras.putBoolean(KEY_SINGLE, true);
		intent.putExtras(extras);
		PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(context,
				SINGLE_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar futureDate = Calendar.getInstance();
		futureDate.add(Calendar.SECOND, 2);

		setSingleExactAlarm(futureDate.getTime().getTime(), pendingUpdateIntent);
	}

	public void scheduleRepeatingAlarm(Context context) {
		Intent intent = new Intent(context, NotificationReceiver.class);
		Bundle extras = new Bundle();
		extras.putBoolean(KEY_REPEAT, true);
		intent.putExtras(extras);
		PendingIntent pIntent = PendingIntent.getBroadcast(context,
				REPEAT_ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar futureDate = Calendar.getInstance();
		futureDate.add(Calendar.SECOND, (int) (INTERVAL_SEVEN_SECONDS / 1000));

		if (android.os.Build.VERSION.SDK_INT >= 19) {
			setSingleExactAlarm(futureDate.getTime().getTime(), pIntent);
		} else {
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate
					.getTime().getTime(), INTERVAL_SEVEN_SECONDS, pIntent);
		}
	}

	public void removeRepeatingAlarm(Context context) {
		Intent intent = new Intent(context,
				NotificationReceiver.class);
		PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(context,
				REPEAT_ALARM_ID, intent, 0);

		mAlarmManager.cancel(pendingUpdateIntent);
	}

	@SuppressLint("NewApi")
	private void setSingleExactAlarm(long time, PendingIntent pIntent) {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
		} else {
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
		}
	}

	public boolean isSingleAlarm(Bundle extras) {
		return extras.containsKey(KEY_SINGLE) && extras.getBoolean(KEY_SINGLE);
	}

	public boolean isRepeatAlarm(Bundle extras) {
		return extras.containsKey(KEY_REPEAT) && extras.getBoolean(KEY_REPEAT);
	}

}
