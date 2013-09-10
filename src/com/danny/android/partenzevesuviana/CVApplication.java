package com.danny.android.partenzevesuviana;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CVApplication extends Application {

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		 PreferenceManager.setDefaultValues(this,
                 R.xml.fragmented_preferences, false);
	}

}
