package net.rdyonline.android_training.intents;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class IntentSecondActivity extends Activity {
	
	TextView tvBundle;
	TextView tvDirect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_second);
		
		tvBundle = (TextView) findViewById(R.id.tv_send_bundle);
		tvDirect = (TextView) findViewById(R.id.tv_send_direct);
		
		loadDetails(); 
	}
	
	private void loadDetails() {
		loadBundle();
		loadDirect();
	}
	
	private void loadBundle() {
		Bundle b = getIntent().getExtras();
		String value = b.getString("test");
		
		tvBundle.setText(value);
	}
	
	private void loadDirect() {
		String value = getIntent().getStringExtra("test");
		
		tvDirect.setText(value);
	}
}
