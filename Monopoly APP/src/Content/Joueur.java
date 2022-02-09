package Content;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import Content.Case.Propriete;

public class Joueur {
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();
	private int id;
	private String pseudo;
	private int argent;
	private int position;
	private int tourPrison;
	private Des des;
	private ArrayList<Propriete> proprietes;
	//private ArrayList<CarteAction> cartes;
	
	//constructeur pour la config
	public Joueur(int id, String pseudo, int argent, int position, int tourPrison, ArrayList<Propriété> proprietes) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
		this.position = position;
		this.tourPrison = tourPrison;
		this.proprietes = proprietes;
	}
	//constructeur simplifié pour la création d'un joueur
	public Joueur(int id, String pseudo, int argent) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
	}
	
	public void lancerDes() {
		int alea=(int)(Math.random() * 6);
		des.setValeur(alea);
	}
	
	public void acheter(Propriete prop) {
	}
	
	public void payer(Propriete prop) {
	}
	
	public void echanger(Propriete prop) {
		
	}
	
	
	
	
}
