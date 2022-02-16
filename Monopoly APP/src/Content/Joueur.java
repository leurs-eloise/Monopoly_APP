package Content;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import Content.DemandeEchangeEvent;
import Content.EchangeEvent;
import Content.Case.Gare;
import Content.Case.Propriete;
import Content.Case.Service;
import Content.Case.Terrain;

public class Joueur {
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();
	private Scanner scan = new Scanner(System.in);
	private int id;
	private String pseudo;
	private int argent;
	private int position;
	private int tourPrison;
	private int valDes;
	private Des des;
	private ArrayList<Propriete> proprietes;
	//private ArrayList<CarteAction> cartes;
	
	//constructeur pour la config
	public Joueur(int id, String pseudo, int argent, int position, int tourPrison, ArrayList<Propriete> proprietes) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
		this.position = position;
		this.tourPrison = tourPrison;
		this.proprietes = proprietes;
	}
	//constructeur simplifie pour la creation d'un joueur
	public Joueur(int id, String pseudo, int argent) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
	}
	
	public void lancerDes() {
		int alea=(int)(Math.random() * 6);
		des.setValeur(alea);
		this.valDes = alea;
	}
	
	public void acheter(Propriete prop) {
		if((prop.getJoueur() == null) && (this.argent >= prop.getPrix())){
			this.argent -= prop.getPrix();
			prop.setJoueur(this);
		}
	}
	
	public void payer(Terrain prop) {
		Joueur proprietaire = prop.getJoueur();
		if((proprietaire != null) && (proprietaire != this)){
			int loyer = prop.getListeLoyer().get(prop.getNbBuilding());
			proprietaire.setArgent(proprietaire.getArgent() + loyer);
			this.setArgent(this.getArgent() - loyer);
			prop.setJoueur(this);
		}
	}
	
	public void payer(Gare prop) {
		Joueur proprietaire = prop.getJoueur();
		if((proprietaire != null) && (proprietaire != this)){
			proprietaire.setArgent(proprietaire.getArgent() + prop.getLoyer());
			this.setArgent(this.getArgent() - prop.getLoyer());
		}
	}
	
	public void payer(Service prop) {
		Joueur proprietaire = prop.getJoueur();
		if((proprietaire != null) && (proprietaire != this)){
			this.lancerDes();
			proprietaire.setArgent(proprietaire.getArgent() + prop.getNiveau()*valDes);
			this.setArgent(this.getArgent() - prop.getNiveau()*valDes);
		}
	}
	
	public void onEventCreated(DemandeEchangeEvent ev) {
		boolean choix = false;
		System.out.println("Acceptez-vous cette l'échange ?");
		String reponse = this.scan.nextLine();
		if (reponse.equals("oui")) {
			choix = true;
		}
	}
	
	public void onEventCreated(EchangeEvent ev) {
		if(ev.isChoix()) {
			this.echanger(ev.getProp());
		}
	}
	
	public void echanger(Propriete prop) {
		Joueur proprietaire = prop.getJoueur();
		if((proprietaire != null) && (proprietaire != this)){
			prop.setJoueur(proprietaire);
			}

		
	}
	
	public void acheterBuilding(int nombre, Terrain ter) {
		if (ter.getJoueur() == this) {
			//if (ter.getBuilding()<4) {
			
			//}
		}
		
		
	}
	
	
	//Getter and setter 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public int getArgent() {
		return argent;
	}
	public void setArgent(int argent) {
		this.argent = argent;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getTourPrison() {
		return tourPrison;
	}
	public void setTourPrison(int tourPrison) {
		this.tourPrison = tourPrison;
	}
	public Des getDes() {
		return des;
	}
	public void setDes(Des des) {
		this.des = des;
	}
	public ArrayList<Propriete> getProprietes() {
		return proprietes;
	}
	public void setProprietes(ArrayList<Propriete> proprietes) {
		this.proprietes = proprietes;
	}
	
	
	
	
}
