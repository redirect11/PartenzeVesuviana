package com.danny.android.partenzevesuviana;


import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.androidquery.AQuery;
import com.danny.android.partenzevesuviana.listeners.OnDownloadedDataListener;
import com.danny.android.partenzevesuviana.model.Treno;
import com.danny.android.partenzevesuviana.utils.Utils;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends SherlockListActivity implements OnDownloadedDataListener{
	
	public static final String URL = "http://93.51.147.28/smart/";
	//public static final String URL = "http://redirectreloaded.com/pn.html";
	
	//private static final int RESULT_SETTINGS = 1;
	
	private TrainsListAdapter adapter;
	private BackgroundData bg;
	private ListView mListView;
	LayoutInflater inflater;
	AQuery aq;
	ActionBar ab;
	String abSub = "";
	boolean connectionError = false;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        ab = getSupportActionBar();
        setContentView(R.layout.activity_main);
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());
        inflater = getLayoutInflater();
        mListView = getListView();
        aq = new AQuery(this);
        mListView.setEmptyView(aq.id(R.id.no_trains).getView());
        updateData();
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    
    public void updateData() {
    	aq.id(R.id.no_trains).text("Caricamento...");
    	if(connectionError) {
    		connectionError = false;
    	}
		setSupportProgressBarIndeterminateVisibility(true);
    	if(this.bg != null) {
    		bg = null;
    	}
    	bg = new BackgroundData(this);	
    	bg.execute();
    }
    
    public void onDataLoaded(ArrayList<Treno> data) {
    	if (data != null) {	
    		if(adapter == null) {
    			adapter = new TrainsListAdapter(this, data);
    			mListView.setAdapter(adapter);
    		} else {
    			adapter.setNewData(data);
    			adapter.notifyDataSetChanged();
    		}
    		if(!abSub.equals("")) {
    			ab.setSubtitle(abSub);
    		}
    		Toast.makeText(this, "Dati aggiornati", Toast.LENGTH_LONG).show();
    	} else {
    		// no data
    	}
    	
    }
    
//    public ArrayList<Treno> filter(ArrayList<Treno> trains) {
//    	Map<String,?> savedPrefs = new HashMap<String,Boolean>();
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//		savedPrefs = prefs.getAll();
//		Iterator iterator = savedPrefs.keySet().iterator();
//		while (iterator.hasNext()) {
//		   String key = iterator.next().toString();
//		   String value = savedPrefs.get(key).toString();
//		   System.out.println(key + " " + value);
//		}
//		return null;
//    }
    
    public class TrainsListAdapter extends BaseAdapter {
    	
    	ArrayList<Treno> mTrains;
    	Context mContext;
    	AQuery aq;
    	
    	public TrainsListAdapter(Context context, ArrayList<Treno> trains) {
    		this.mTrains = trains;
    		this.mContext = context;
    	}

		@Override
		public int getCount() {
			return mTrains.size();
		}

		@Override
		public Object getItem(int position) {
			return mTrains.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Treno t = mTrains.get(position);
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.departure_list_row, null);
			}
			AQuery aq = new AQuery(convertView);
			aq.id(R.id.dest).text(t.destinazione);
			aq.id(R.id.ora).text(Utils.dateToString(t.orario));
			TextView s = aq.id(R.id.stato).getTextView();
			if(t.stato.equals("SOPPRESSO")) {
				s.setTextColor(getResources().getColor(R.color.supp));
			} else {
				s.setTextColor(getResources().getColor(R.color.regular));
			}
			aq.id(R.id.stato).text(t.stato);
			aq.id(R.id.cat).text(Utils.getCat(t.cat));
			aq.id(R.id.bin).text("Binario: " + t.bina);
			return convertView;
		}
		
		public void setNewData(ArrayList<Treno> trains) {
			this.mTrains.clear();
			this.mTrains.addAll(trains);
			//Log.d("MainActivity", "setNewData() Called");
		}
    	
    }

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockListActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.update:
			updateData();
			return true;
//		case R.id.menu_settings:
//			Intent i = new Intent(MainActivity.this, FilterSettingsActivity.class);
//			startActivityForResult(i, RESULT_SETTINGS);
//			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDataReady(Object data) {
		if(data instanceof ArrayList<?>) {
			if(!connectionError) {
				if(((ArrayList<Treno>) data).size() == 0) {
					aq.id(R.id.no_trains).text("Servizio terminato.\nNessun treno in partenza");	
				}
				onDataLoaded((ArrayList<Treno>) data);
			} else {
				aq.id(R.id.no_trains).text("Errore di connessione.\nPremi il tasto aggiorna per riprovare");
			}
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}
		

	@Override
	public Object asyncTask() {
		ArrayList<Treno> trains = new ArrayList<Treno>();
		DeparturesParser p = new DeparturesParser(URL);
		try {
			p.downloadDoc();
			trains = p.getTrains();
			abSub = p.getLastUpdate();
			//ArrayList<String> destinations = p.findUniqueDestinations();
//			for(int i = 0; i < destinations.size(); i++) {
//				Log.d("Destinazione " + i, destinations.get(i));
//			}
		} catch (IOException e) {
			connectionError = true;
			return trains;	
		}
		return trains;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//	}
	
	
}
