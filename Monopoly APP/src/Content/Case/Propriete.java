package Content.Case;

import java.util.ArrayList;

import Content.Joueur;

public class Propriete implements Case {
	private int prix;
	private Joueur joueur;
	private ArrayList<Integer> loyer = new ArrayList<>();
	
	// Constructeur
	public void Propriete(int prix) {
		this.prix = prix;
	}
	
	// Getter & Setter
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
