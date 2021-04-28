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
	public List<Citta> citta;

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
//		setCitta(mese);
		List<Citta> parziale = new ArrayList<>();
		 citta= setCitta(mese);
		 this.result=null;
		int livello = 0;
		
		int costo = 0;
//		costoRicorsivo(parziale,citta,costo,livello);
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
		if(livello==NUMERO_GIORNI_TOTALI) {
			int costo= calcolaCosto(parziale);
			if(costo<min) {
				min=costo;
			List<Citta> finale=new ArrayList<>(parziale);
			this.result=finale.toString();
			}
//			return;
		}
		else {
			for(Citta c: citta) {
//				System.out.println(c.getCounter());
//				if(c.getCounter()<6) {
					
					
				if(valido(parziale,c)) {

					parziale.add(c);
//					int newCosto= costo +c.getRilevamento(livello).getUmidita();
//					if(parziale.size()>0) {
//					
////										newCosto= newCosto+COST;
//									}
//									else if(parziale.get(livello-1).equals(c))
//										newCosto= newCosto+COST;
				
				
					ricercaCosto(parziale,livello+1);
					
					parziale.remove(parziale.size()-1);
					
				}
				
				

//					
					}
				
			}
			
		}
	
	

	
	public List<Citta> setCitta(int mese) {
		List<Citta> citta= new ArrayList<>();
//		Citta Torino= new Citta("Torino",md.getAllRilevamentiLocalitaMese(mese,"Torino"));
//		Citta Milano= new Citta("Torino",md.getAllRilevamentiLocalitaMese(mese,"Milano"));
//		Citta Genova= new Citta("Torino",md.getAllRilevamentiLocalitaMese(mese,"Genova"));
		citta.add(new Citta("Torino",md.getAllRilevamentiLocalitaMese(mese,"Torino")));
		citta.add(new Citta("Milano",md.getAllRilevamentiLocalitaMese(mese,"Milano")));
		citta.add(new Citta("Genova",md.getAllRilevamentiLocalitaMese(mese,"Genova")));
		return citta;
	}
	
	public boolean valido(List<Citta> parziale,Citta c) {
		System.out.println(parziale.size());
		System.out.println(c);
		System.out.println(parziale);
		int conta =0;
		for(Citta precedente:parziale) {
			if(precedente.equals(precedente))
				conta++;
		}
		if(conta>=NUMERO_GIORNI_CITTA_MAX)
			return false;
		if(c.getCounter()==0)
			return true;
		if(parziale.size()==1||parziale.size()==2) {
			//siamo al secondo o terzo giorno, non posso cambiare
			//quindi l'aggiunta è valida solo se la città di prova coincide con la sua precedente
			return parziale.get(parziale.size()-1).equals(c);
		}
		//nel caso generale, se ho già passato i controlli sopra, non c'è nulla che mi vieta di rimanere nella stessa città
		//quindi per i giorni successivi ai primi tre posso sempre rimanere
		if (parziale.get(parziale.size()-1).equals(c))
					return true; 
		
		// se cambio città mi devo assicurare che nei tre giorni precedenti sono rimasto fermo 
				if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
				&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
					return true;
		
		
	
			return false;
		}
	private int calcolaCosto(List<Citta> parziale) {
		int costo = 0;
		//sommatoria delle umidità in ciascuna città, considerando il rilevamento del giorno giusto
		//SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		for (int giorno=1; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			//dove mi trovo
			Citta c = parziale.get(giorno-1);
			//che umidità ho in quel giorno in quella città?
			int umid = c.getRilevamenti().get(giorno-1).getUmidita();
			costo+=umid;
		}
		//poi devo sommare 100*numero di volte in cui cambio città
		for (int giorno=2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			//dove mi trovo
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo +=COST;
			}
		}
		return costo;
	}

	}
	