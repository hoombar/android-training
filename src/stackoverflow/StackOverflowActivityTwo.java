package stackoverflow;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class StackOverflowActivityTwo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stackoverflow_two);
		
		readFromSharedPrefs();
	}
	
	private void readFromSharedPrefs() {
	    SharedPreferences settings;
		SharedPreferences.Editor editor;
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		String value = settings.getString("auth_token", "Nothing");
		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
	}
	
}
