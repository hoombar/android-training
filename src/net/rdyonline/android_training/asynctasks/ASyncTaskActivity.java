package net.rdyonline.android_training.asynctasks;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ASyncTaskActivity extends Activity {

	private static final String TAG = ASyncTaskActivity.class.getSimpleName();
	
	private ProgressBar mProgress;
	private Button mUnsafeTask;
	private Button mSafeTask;
	private Button mProgressTask;
	private Button mSerialTask;
	private Button mParallelTask;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asynctask);
		
		mContext = this;
		
		bindViews();
		setListeners();
	}
	
	@Override
	protected void onDestroy() {
		Toast.makeText(mContext,
				"Activity destroyed", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	
	private void bindViews() {
		mProgress = (ProgressBar) findViewById(R.id.async_progress);
		mUnsafeTask = (Button) findViewById(R.id.button_unsafe_async_task);
		mSafeTask = (Button) findViewById(R.id.button_safe_async_task);
		mProgressTask = (Button) findViewById(R.id.button_progress_async_task);
		mSerialTask = (Button) findViewById(R.id.button_async_task_serial);
		mParallelTask = (Button) findViewById(R.id.button_async_task_parallel);
	}
	
	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_safe_async_task:
					launchSafeTask();
					break;
				case R.id.button_unsafe_async_task:
					launchUnsafeTask();
					break;
				case R.id.button_progress_async_task:
					launchProgressTask();
					break;
				case R.id.button_async_task_serial:
					launchSerial();
					break;
				case R.id.button_async_task_parallel:
					launchParallel();
					break;
				}
			}
		};
		
		mUnsafeTask.setOnClickListener(listener);
		mSafeTask.setOnClickListener(listener);
		mProgressTask.setOnClickListener(listener);
		mSerialTask.setOnClickListener(listener);
		mParallelTask.setOnClickListener(listener);
	}
	
	private void launchSafeTask() {
		new SafeASyncTask<Void>(this) {

			@Override
			protected Void onRun() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onSuccess(Void result) {
				Toast.makeText(mContext,
						"Safe", Toast.LENGTH_LONG).show();
			}
		}.execute();
	}
	
	private void launchUnsafeTask() {
		new UnsafeASyncTask() {
			
			@Override
			protected void onPostExecute(Void result) {
				Toast.makeText(mContext,
						"Unsafe - activity still exists..", Toast.LENGTH_SHORT).show();
			};
			
		}.execute();
	}
	
	private void launchProgressTask() {
		mProgress.setProgress(0);
		
		// Params, Progress, Result
		new AsyncTask<Void, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				int counter = 0;
				while (counter < 6) {
					publishProgress(new Integer[] { counter });
					counter++;
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return counter;
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				mProgress.setProgress(values[0]);
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				mProgress.setProgress(result);
				
				Toast.makeText(mContext, "Progress done..", Toast.LENGTH_SHORT).show();
			}
			
		}.execute();
	}
	
	/***
	 * After HoneyComb uses serial by default
	 * After GB, before HoneyComb uses parallel by default
	 */
	private void launchParallel() {
		for (int i = 0; i < 10; i++) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					for (int i = 0; i < 1000; i++) {
						Log.i(TAG, Integer.toString(i));
					}
					return null;
				}
				
			}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
		Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * After HoneyComb uses serial by default
	 * After GB, before HoneyComb uses parallel by default
	 */
	private void launchSerial() {
		for (int i = 0; i < 10; i++) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					for (int i = 0; i < 1000; i++) {
						Log.i(TAG, Integer.toString(i));
					}
					return null;
				}
				
			}.execute();
		}
		
		Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
	}
}
