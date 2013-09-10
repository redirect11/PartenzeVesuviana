package com.danny.android.partenzevesuviana;

import android.os.AsyncTask;

import com.danny.android.partenzevesuviana.listeners.OnDownloadedDataListener;

public class BackgroundData extends AsyncTask<Void,Void,Object> {


	private OnDownloadedDataListener listener;


	public BackgroundData(OnDownloadedDataListener listener) {
		this.listener = listener;
	}

	@Override
	protected Object doInBackground(Void... params) {
		return listener.asyncTask();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Object data) {
		this.listener.onDataReady(data);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		 
		//super.onPreExecute();
	}
	
}