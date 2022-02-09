package Content.Case;

import Content.Joueur;

public class Propriete implements Case {
	private int prix;
	private Joueur joueur;
	
	// Constructeur
	public void Propriete(int prix, Joueur joueur) {
		this.prix = prix;
		this.joueur = joueur;
	}
	
	// Getter & Setter
	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	


}
