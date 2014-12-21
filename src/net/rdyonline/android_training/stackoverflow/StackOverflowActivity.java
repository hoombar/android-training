package net.rdyonline.android_training.stackoverflow;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StackOverflowActivity extends Activity {

	private Button btnClickMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stackoverflow);
		
		bindViews();
		setListeners();
	}
	
	private void bindViews() {
		btnClickMe = (Button) findViewById(R.id.btn_click_me);
	}
	
	private void setListeners() {
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_click_me:
					saveToPrefs();
					launchSecondActivity();
					break;
				}
			}
		};
		
		btnClickMe.setOnClickListener(listener);
	}
	
	private void saveToPrefs() {
		SharedPreferences settings;
		SharedPreferences.Editor editor;
		settings = PreferenceManager.getDefaultSharedPreferences(this);
	    editor = settings.edit();
	    editor.putString("auth_token", "RESULT");
	    editor.commit();
	}
	
	private void launchSecondActivity() {
		Intent intent = new Intent(this, StackOverflowActivityTwo.class);
		startActivity(intent);
	}
	
}
