package Content;

import java.util.ArrayList;
import java.util.Objects;

public class Partie {
	private ArrayList<Joueur> listeJoueur;
	private Joueur joueurActuel;
	private static Partie partie;
	private int etat = 0;
	
	public Partie() {
	}
	public static Partie getInstance() {
		if(Objects.isNull(partie)) {
			partie = new Partie();
		}
		return partie;
	}
	public Joueur getCurrentPlayer() {
		return joueurActuel;
	}
	
	//init
	public void init() {
		
		etat = 1;
	}

	public void lancerDes() {
		
		
		etat = 2;
	}
	public void acheter() {
		
		
		etat = 3;
	}
	public void skip() {
		etat = 3;
		finTour();
	}
	public void hypotheque() {
		
		etat = 3;
	}
	public void echanger() {
		
		
		etat = 3;
	}
	public void acheterBuilding(int level) {
		
		etat = 3;
	}
	public void finTour() {
		
		
		etat = 1;
	}
	
	

}
