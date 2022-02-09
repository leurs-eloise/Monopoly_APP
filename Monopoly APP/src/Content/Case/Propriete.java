package Content.Case;

import java.util.ArrayList;

import Content.Joueur;

public class Propriete implements Case {
	private int id;
	private int prix;
	private Joueur joueur;
	private ArrayList<Integer> loyer = new ArrayList<>();
	
	// Constructeur
	public Propriete(int id, int prix, Joueur j) {
		this.id = id;
		this.prix = prix;
		this.joueur = null;
	}
	
	
	// Getter & Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public Joueur getJoueur() {
		if (this.joueur == null) {
			return null;
		}
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public ArrayList<Integer> getLoyer() {
		return loyer;
	}

	public void setLoyer(ArrayList<Integer> loyer) {
		this.loyer = loyer;
	}
	


}
