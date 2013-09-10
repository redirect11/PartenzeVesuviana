package com.danny.android.partenzevesuviana;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.danny.android.partenzevesuviana.model.Treno;
import com.danny.android.partenzevesuviana.utils.Utils;

public class DeparturesParser {

	Document doc;
	String url;
	ArrayList<Treno> mTrains;

	public DeparturesParser(String url) {
		this.url = url;
	}

	public void downloadDoc() throws IOException {
		this.doc = Jsoup.connect(url).timeout(7000).get();
	}

	public ArrayList<Treno> getTrains() {
		ArrayList<Treno> trains = new ArrayList<Treno>();
		if (this.doc == null) {
			try {
				downloadDoc();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// Recupero la prima tabella della pagina
		Elements tables = this.doc.select("table#tablex");
		if (tables != null && tables.size() > 0) {
			// Recupero i prossimi treni in partenza
			Element e = tables.get(0);
			if (e != null) {
				// Recupero il body della tabella
				Elements e2 = e.select("tbody tbody");
				if (e2 != null) {
					Elements status = null;
					Elements ftRows = null;
					Elements stRows = null;
					if (e2.size() > 0)
						status = e2.get(0).select("tr td.Cella");
					if (e2.size() > 1) {
						ftRows = e2.get(1).select("tr");
						if (e2.size() > 2)
							stRows = e2.get(2).select("tr");

						if (status != null) {
							for (int i = 0; i < status.size(); i++) {
								// Log.d("status" + i, status.get(i).html());
							}
						}

						// Aggiungo i treni della tabella a sinistra
						if (ftRows != null) {
							ftRows.remove(0);
							for (int i = 0; i < ftRows.size(); i++) {
								// Log.d("ftRows" + i, ftRows.get(i).html());
								Treno t = new Treno(ftRows.get(i).select(
										"td.Cella"), status.get(i).text());
								trains.add(t);
								// Log.d("treno " + i, "dest :" +
								// t.destinazione);
							}
						}

						// Aggiungo i treni della tabella a destra
						if (stRows != null) {
							stRows.remove(0);
							for (int i = 0; i < stRows.size(); i++) {
								// Log.d("stRows" + i, stRows.get(i).html());
								Treno t = new Treno(stRows.get(i).select(
										"td.Cella"), "");
								trains = addIfNotExistsTrain(trains, t);
							}
						}
					}
				}
				// Aggiungo i treni soppressi
				Element supp;
				if (tables.size() == 2) {
					supp = tables.get(1);
				} else {
					if (trains.size() > 0) {
						return trains;
					} else {
						supp = tables.get(0);
					}
				}

				if (supp != null) {
					Elements supp2 = supp.select("tbody tbody");
//					for (int i = 0; i < supp2.size(); i++) {
//						Log.d("supp2 " + i, supp2.get(i).html());
//					}
					Elements ftSuppRows = supp2.get(0).select("tr");
					if (ftSuppRows != null) {
						ftSuppRows.remove(0);
						for (int i = 0; i < ftSuppRows.size(); i++) {
							Treno t = new Treno(ftSuppRows.get(i).select(
									"td.Cella"), "SOPPRESSO");
							trains.add(t);
						}
					}
				}

			}
			// Riordino in base all'orario
			Collections.sort(trains);
			if (this.mTrains != null) {
				this.mTrains.clear();
				this.mTrains = null;
			}
			this.mTrains = trains;
		}
		return trains;
	}

	/**
	 * 
	 * @param trains
	 * @param t
	 * @return
	 */
	public ArrayList<Treno> addIfNotExistsTrain(ArrayList<Treno> trains, Treno t) {
		boolean found = false;
		int i = 0;
		while (!found && trains.size() > i) {
			if (isTheSameTrain(trains.get(i), t)) {
				found = true;
			}
			i++;
		}
		if (!found) {
			trains.add(t);
		}

		return trains;
	}

	public boolean isTheSameTrain(Treno uno, Treno due) {
		boolean yes = false;
		if (uno.destinazione.equals(due.destinazione)
				&& uno.via.equals(due.via)
				&& uno.orario.compareTo(due.orario) == 0
				&& (!uno.stato.equals("SOPPRESSO") || !due.stato
						.equals("SOPPRESSO"))) {
			yes = true;
			// Log.d("sono uguali", "sono uguali");
		}
		return yes;
	}

	public String getLastUpdate() {
		String update = "Nessun aggiornamento";
		if (this.doc == null) {
			try {
				downloadDoc();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Element e = this.doc.select("span#testo").first();
		if (e != null) {
			update = e.text();
		}
		return update;

	}

	public ArrayList<String> findUniqueDestinations() {
		ArrayList<String> destinations = new ArrayList<String>();
		if (this.mTrains == null) {
			getTrains();
		}
		if (this.mTrains != null && this.mTrains.size() > 0) {
			for (int i = 0; i < this.mTrains.size(); i++) {
				destinations.add(mTrains.get(i).destinazione);
			}
		}
		destinations = Utils.removeDuplicate(destinations);
		return destinations;
	}

}
