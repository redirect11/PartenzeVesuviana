package com.danny.android.partenzevesuviana.model;

import java.util.Date;

import org.jsoup.select.Elements;

import android.util.Log;

import com.danny.android.partenzevesuviana.utils.Utils;

public class Treno implements Comparable<Treno>{


	public String stato;
	public String destinazione;
	public String via;
	public String cat;
	public Date orario;
	public String bina;
	
	/**
	 * @param stato
	 * @param destinazione
	 * @param via
	 * @param cat
	 * @param orario
	 * @param bina
	 */
	public Treno(String stato, String destinazione, String via, String cat,
			String orario, String bina) {
		super();
		this.stato = stato;
		this.destinazione = destinazione;
		this.via = via;
		this.cat = cat;
		this.orario = Utils.stringToDate(orario);
		this.bina = bina;
	}
	
	public Treno (Elements row, String status) {
		this.stato = status;
		this.destinazione = row.get(0).text();
		this.via = row.get(1).text();
		this.cat = row.get(2).text();
		this.orario = Utils.stringToDate(row.get(3).text());
		try{
			this.bina = row.get(4).text();
			if(row.get(4).html().equals("&nbsp;")) this.bina="N.P.";
			//Log.d("binario", row.get(4).html());
		} catch(IndexOutOfBoundsException e) {
			this.bina="N.P.";
		}
	}


	@Override
	public int compareTo(Treno treno) {
		if(treno instanceof Treno) {
			Treno t = (Treno) treno;
			return this.orario.compareTo(t.orario);
		}
		return 0;
	}
	
}
