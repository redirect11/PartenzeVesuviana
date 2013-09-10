package com.danny.android.partenzevesuviana;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class FilterSettingsActivity extends SherlockPreferenceActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        addPreferencesFromResource(R.xml.fragmented_preferences);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
    
    
    
    /**
     * This fragment shows the preferences for the first header.
     */
//    public static class DestinationFilterSettingsFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
//            
//            // Load the preferences from an XML resource
//            addPreferencesFromResource(R.xml.fragmented_preferences);
//        }
//    }



	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockPreferenceActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.save:
			showSuccesfullSavedMessage();
			finish();
		default: 
			return super.onOptionsItemSelected(item);
		}
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		showSuccesfullSavedMessage();
		super.onBackPressed();
	}
	
	
	public void showSuccesfullSavedMessage() {
		Toast.makeText(this, "Filtri salvati", Toast.LENGTH_LONG).show();
	}


}
