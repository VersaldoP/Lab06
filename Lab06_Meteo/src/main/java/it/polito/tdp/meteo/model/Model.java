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
		List<Citta> citta= setCitta(mese);
		int livello = 0;
		
		int costo = 0;
//		costoRicorsivo(parziale,citta,costo,livello);
		ricercaCosto(parziale,citta,costo,livello);
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
	
	
	public void ricercaCosto(List<Citta>parziale,List<Citta> citta,int costo,int livello) {
		//Codizone Terminale
		if(parziale.size()==NUMERO_GIORNI_TOTALI) {
			if(costo<min) {
				min=costo;
			List<Citta> finale=new ArrayList<>(parziale);
			this.result=finale.toString();
			}
			return;
		}
		else {
			for(Citta c: citta) {
				//Genero la soluzione Parziale
				if(c.getCounter()<6) {
					if(c.getCounter()<3) {
					parziale.add(c);
					parziale.add(c);
					parziale.add(c);
					int counter = c.getCounter();
					c.setCounter(counter+3);
					int newCosto= costo
									+c.getRilevamento(livello).getUmidita()
									+c.getRilevamento(livello+1).getUmidita()
								    +c.getRilevamento(livello+2).getUmidita();
					
//					System.out.println(newCosto);
//					System.out.println(parziale);
					if(parziale.size()==3) {
//	
//						newCosto= newCosto+COST;
					}
					else if(c!=parziale.get(livello-1))
						newCosto= newCosto+COST;
					System.out.println(parziale.size());
					System.out.println(livello);
					System.out.println(parziale);
					System.out.println(costo+"---> "+newCosto);
					ricercaCosto(parziale,citta,newCosto,livello+3);
					//backtracking
					c.setCounter(counter);
					parziale.remove(c);
					parziale.remove(c);
					parziale.remove(c);
				}
//					else {
//						 if(c!=parziale.get(livello-1)&&c.getCounter()==3) {
//							 	parziale.add(c);
//								parziale.add(c);
//								parziale.add(c);
//								int counter = c.getCounter();
//								c.setCounter(counter+3);
//								int newCosto= costo
//												+c.getRilevamento(livello).getUmidita()
//												+c.getRilevamento(livello+1).getUmidita()
//											    +c.getRilevamento(livello+2).getUmidita();
//								
//
//									newCosto= newCosto+COST;
//								System.out.println(parziale.size());
//								System.out.println(livello);
//								System.out.println(parziale);
//								ricercaCosto(parziale,citta,newCosto,livello+3);
//								//backtracking
//								c.setCounter(counter);
//								parziale.remove(c);
//								parziale.remove(c);
//								parziale.remove(c);
//						}
						 else {
						parziale.add(c);
						int counter = c.getCounter();
						c.setCounter(counter+1);
						int newCosto= costo+c.getRilevamento(livello).getUmidita();
										
						System.out.println(parziale.size());
						System.out.println(livello);
						System.out.println(parziale);
//						if(c!=parziale.get(livello))
//							newCosto= newCosto+COST;
						ricercaCosto(parziale,citta,newCosto,livello+1);
						//backtracking
						c.setCounter(counter);
						parziale.remove(c);
						 }
					
					}
				}
			}
			
		}
	
	
//	public void costoRicorsivo(List<Citta> parziale,List<Citta>citta,int costo, int livello) {
////		System.out.println(livello);
////		System.out.println(parziale);
////		System.out.println(citta);
//		if(livello>NUMERO_GIORNI_TOTALI) {
//			System.out.println("check");
//			return;
//		}
//		if(livello==NUMERO_GIORNI_TOTALI&&parziale.containsAll(citta)) {
//			int min=MAX_VALUE;
//			
//			if(costo<min) {
////				List<Citta> finale = new ArrayList<>(parziale);
//				this.result=parziale.toString();
//			}
//			return;
//			
//		
//		}
//		else{
//			for(Citta c:citta) {
//				if(valido(parziale,c,livello)) {
//					parziale.add(c);
////					System.out.println(parziale);
////					System.out.println(c);
//					c.increaseCounter();
//					int newcosto= costo + c.getRilevamento(livello).getUmidita();
//					if(parziale.size()>1) {
//					if(!c.equals(parziale.get(livello-1))){
//						newcosto = newcosto + 100;
//					}
//					}
//					costoRicorsivo(parziale,citta,newcosto,livello++);
//					parziale.remove(c);
//					c.setCounter(c.getCounter()-1);
//				}
////					if(parziale.size()==0) {
////						parziale.add(c.toString());
////						c.increaseCounter();
////					}
////					else {
////						if(parziale.size()==1&&c.equals(parziale.get(parziale.size()-1))) {
////							parziale.add(c.toString());
////							c.increaseCounter();
////						}
////						else {
////							if((c.equals(parziale.get(parziale.size()-2)))&&(c.equals(parziale.get(parziale.size()-1)))) {
////								parziale.add(c.toString());
////								c.increaseCounter();
////							}
////						}
////					}
////				}
//			}
//		}
//	}
	
	
	
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
	
//	public boolean valido(List<Citta> parziale,Citta c,int livello) {
//		System.out.println(parziale.size());
//		System.out.println(c);
//		System.out.println(parziale);
//		
//		if((c.getCounter()<=6)&&((parziale.size()==0)
//				||(parziale.size()==1&&c.equals(parziale.get(parziale.size()-1)))
//				||((c.equals(parziale.get(parziale.size()-2)))&&(c.equals(parziale.get(parziale.size()-1)))))) {
//				return true;
//			}
//		else {
//			return false;
//		}
//		if(livello==0) {
//		return true;
//		}
//			else {
//				System.out.println(parziale);
//				System.out.println(c);
//				if(c.equals(parziale.get(livello- 1)))
//						System.out.println("check");
//
//				if (c.getCounter() <= 6) {
//					System.out.println(c.getCounter());
//					
//
//					if (livello== 1 && c.equals(parziale.get(livello- 1))) {
//						System.out.println(parziale.get(0));
//						return true;
//
//					} else {
//						if ((c.equals(parziale.get(livello - 2)))
//								&& (c.equals(parziale.get(livello- 1)))) {
//							return true;
//
//						}
//
//					}
//				}
//			}
//			return false;
//		}
	}
	