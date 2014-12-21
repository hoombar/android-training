package net.rdyonline.android_training.intents;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntentActivity extends Activity {
	
	Button btnSendBundle;
	Button btnSendDirect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		
		btnSendBundle = (Button) findViewById(R.id.btn_send_bundle);
		btnSendDirect = (Button) findViewById(R.id.btn_send_direct);
		
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchSecondActivity(v.getId());
			}
		};
		
		btnSendBundle.setOnClickListener(listener);
		btnSendDirect.setOnClickListener(listener);
	}
	
	private void launchSecondActivity(int viewId) {
		Intent intent = new Intent(this, IntentSecondActivity.class);
		
		switch (viewId) {
		case R.id.btn_send_bundle:
			intent.putExtra("test", "test");
			break;
		case R.id.btn_send_direct:
			Bundle bundle = new Bundle();
			bundle.putString("test", "bundle");
			intent.putExtras(bundle);
			break;
		}
		
		startActivity(intent);
	}
	
}
