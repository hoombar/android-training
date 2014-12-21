package net.rdyonline.android_training.landing;

import net.rdyonline.android_training.R;
import net.rdyonline.android_training.alarms.AlarmActivity;
import net.rdyonline.android_training.asynctasks.ASyncTaskActivity;
import net.rdyonline.android_training.greendao.GreenDaoActivity;
import net.rdyonline.android_training.material.palette.PaletteActivity;
import net.rdyonline.android_training.stackoverflow.StackOverflowActivity;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

	private View mRootView;

	private Button mAsyncDemo;
	private Button mGreenDaoDemo;
	private Button mAlarmDemo;
	private Button mPaletteDemo;
	private Button mStackOverflowPlayground;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_main, container, false);

		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		bindViews();
		setListeners();
	}

	private void bindViews() {
		mAsyncDemo = (Button) mRootView.findViewById(R.id.button_async_demo);
		mGreenDaoDemo = (Button) mRootView
				.findViewById(R.id.button_greendao_demo);
		mAlarmDemo = (Button) mRootView.findViewById(R.id.button_alarm_demo);
		mPaletteDemo = (Button) mRootView
				.findViewById(R.id.button_palette_demo);
		mStackOverflowPlayground = (Button) mRootView
				.findViewById(R.id.button_stackoverflow_playground);
	}

	/**
	 * Wire up the training buttons to launch the topic
	 */
	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_async_demo:
					launchActivity(ASyncTaskActivity.class);
					break;
				case R.id.button_greendao_demo:
					launchActivity(GreenDaoActivity.class);
					break;
				case R.id.button_alarm_demo:
					launchActivity(AlarmActivity.class);
					break;
				case R.id.button_palette_demo:
					launchActivity(PaletteActivity.class);
					break;
				case R.id.button_stackoverflow_playground:
					launchActivity(StackOverflowActivity.class);
					break;
				}
			}

		};

		mAsyncDemo.setOnClickListener(listener);
		mGreenDaoDemo.setOnClickListener(listener);
		mAlarmDemo.setOnClickListener(listener);
		mPaletteDemo.setOnClickListener(listener);
		mStackOverflowPlayground.setOnClickListener(listener);
	}

	/**
	 * Launch a new {@link Activity} to demonstrate usage of training topic
	 * 
	 * @param klass
	 *            the activity class to pass through to launcher intent
	 */
	private void launchActivity(Class<?> klass) {
		Intent launcher = new Intent(getActivity(), klass);
		getActivity().startActivity(launcher);
	}
}