package Content;

import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
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
	private int position = 0;
	private int tourPrison = 0;
	private int valDes;
	private Des des;
	private ArrayList<Propriete> proprietes;
	private int score;
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
		this.setScore();
	}
	//constructeur simplifie pour la creation d'un joueur
	public Joueur(int id, String pseudo, int argent) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
	}
	
	public void lancerDes() {
		Random rand = new Random();
		int alea = rand.nextInt(6)+1;
		//des.setValeur(alea);
		this.valDes = alea;
	}
	
	public void acheter(Propriete prop) {
		if((prop.getJoueur() == null) && (this.argent >= prop.getPrix())){
			this.argent -= prop.getPrix();
			prop.setJoueur(this);
		}
	}
	
	public int getValDes() {
		return valDes;
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
		System.out.println("Acceptez-vous cette l'echange ?");
		String reponse = this.scan.nextLine();
		if (reponse.equals("oui")) {
			choix = true;
		}
	}
	
	public void onEventCreated(EchangeEvent ev) {
		if(ev.isChoix()) {
			this.echanger(ev.getProp1(), ev.getProp2());
		}
	}
	
	public void echanger(Propriete prop1, Propriete prop2) {
		Joueur proprietaire = prop1.getJoueur();
		Joueur destinataire = prop2.getJoueur();
		if((proprietaire != null) && (proprietaire != this)){
			prop1.setJoueur(destinataire);
			prop2.setJoueur(proprietaire);
			}		
	}
	
	public void acheterBuilding(int nombre, Terrain ter) {
		if (ter.getJoueur() == this) {
			if (((ter.getNbBuilding() + nombre)<5) && (this.argent >= Configuration.getInstance().getPrixConstruction(ter.getId()))) {
					this.argent -= Configuration.getInstance().getPrixConstruction(ter.getId());
					ter.setNbBuilding(nombre);
				}			
			}		
	}
	
	
	//Getter and setter 
	public void setScore() {
		this.score = this.argent;
		for (Propriete prop : this.getProprietes()) {
			this.score += prop.getPrix();
			if (prop instanceof Terrain) {
				this.score += ((Terrain)prop).getNbBuilding() * Configuration.getInstance().getPrixConstruction(prop.getId());
			}
		}
	}
	public int getScore() {
		return this.score;
	}
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
