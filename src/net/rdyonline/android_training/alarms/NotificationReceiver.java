package net.rdyonline.android_training.alarms;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.Toast;

/***
 * This is what handles the alarm once it has been received This must be
 * registered in the AndroidManifest.xml
 */
public class NotificationReceiver extends BroadcastReceiver {

	public NotificationReceiver() {
	}

	@Override
	public void onReceive(android.content.Context context,
			android.content.Intent intent) {

		WrappedAlarmManager am = new WrappedAlarmManager(context);

		Bundle extras = intent.getExtras();
		if (am.isSingleAlarm(extras)) {

			Toast.makeText(context, "Single alarm", Toast.LENGTH_SHORT).show();

		} else if (am.isRepeatAlarm(extras)) {

			Toast.makeText(context, "Repeat alarm", Toast.LENGTH_SHORT).show();

			if (android.os.Build.VERSION.SDK_INT >= 19) {
				am.scheduleRepeatingAlarm(context);
			}
		}
	}
}