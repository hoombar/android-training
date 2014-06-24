package net.rdyonline.android_training;

import android.app.Application;
import android.content.Context;

public class AndroidTraining extends Application {

	private static AndroidTraining instance;
	
	public AndroidTraining() {
		instance = this;
	}
	
	public static Context getAppContext() {
		return instance;
	}
	
}
