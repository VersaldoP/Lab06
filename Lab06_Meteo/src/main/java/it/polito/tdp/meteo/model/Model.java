package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private static final int MAX_VALUE = 100000;
	MeteoDAO md = new MeteoDAO();
	private String result;
	private int min= MAX_VALUE;
	private List<Citta> citta;

	public Model() {
		

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String rsT =  "Per la località di Torino :"+calcolaMedia(md.getAllRilevamentiLocalitaMese(mese,"Torino"));
		String rsG =  "Per la località di Genova :"+calcolaMedia(md.getAllRilevamentiLocalitaMese(mese,"Genova"));
		String rsM =  "Per la località di Milano :"+calcolaMedia(md.getAllRilevamentiLocalitaMese(mese,"Milano"));
		String rs= rsT+"\n"+rsG+"\n"+rsM;
		return rs;
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {

		List<Citta> parziale = new ArrayList<>();
		 citta= setCitta(mese);
		int livello = 0;
		
		int costo = 0;

		ricercaCosto(parziale,livello);
		System.out.println(min);
		return result;
	}
	
	public double calcolaMedia(List<Rilevamento> l) {
		double sum =0.0;
		for(Rilevamento r:l) {
			 sum = sum + r.getUmidita();
		}
		return (sum/l.size());
	}
	
	
	public void ricercaCosto(List<Citta>parziale,int livello) {
		//Codizone Terminale
		if(parziale.size()==NUMERO_GIORNI_TOTALI) {
			int costo = calcolaCosto(parziale);
			if(costo<min) {
				min=costo;
				System.out.println(min);
			List<Citta> finale=new ArrayList<>(parziale);
			this.result=finale.toString();
			}
			
		}
		else {
			for(Citta c: citta) {
				//Genero la soluzione Parziale
				if(valido(c,parziale)) {
					parziale.add(c);
					ricercaCosto(parziale,livello++);
					parziale.remove(parziale.size()-1);
					
					
				}
			}
			}
			
		}
	
	
private int calcolaCosto(List<Citta> parziale) {
		int costo= 0;
		
		for(int giorno=1;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			Citta c= parziale.get(giorno-1);
			int rilevamento = c.getRilevamento(giorno-1).getUmidita();
			costo+=rilevamento;
		}
		for(int giorno=2;giorno<=NUMERO_GIORNI_TOTALI;giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2)))
			costo+=COST;
		}
		
		return costo;

	}


	
	private boolean valido(Citta c, List<Citta> parziale) {
		//verica i giorni massimi 
		//contiamo qualte volte la citta c è comparsa nella soluzione parziale
		int conta =0;
		for(Citta precedente:parziale) {
			if(precedente.equals(c))
				conta++;
			
				
		}
		if(conta>=NUMERO_GIORNI_CITTA_MAX)
			return false;
		if(parziale.size()==0)
			return true;
		if(parziale.size()==1||parziale.size()==2) {
			//vuol dire che siamo al secondo o al terzo giorno e quindi non possiamo cambiare citta
			return c.equals(parziale.get(parziale.size()-1));
		}
		if(c.equals(parziale.get(parziale.size()-1)))
			return true;
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2))&&
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
	}

	public List<Citta> setCitta(int mese) {
		List<Citta> citta= new ArrayList<>();

		citta.add(new Citta("Torino",md.getAllRilevamentiLocalitaMese(mese,"Torino")));
		citta.add(new Citta("Milano",md.getAllRilevamentiLocalitaMese(mese,"Milano")));
		citta.add(new Citta("Genova",md.getAllRilevamentiLocalitaMese(mese,"Genova")));
		return citta;
	}
	

	}
	