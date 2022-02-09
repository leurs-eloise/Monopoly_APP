package Content.Case;

import Content.Joueur;

public class Propriété implements Case {
	private int prix;
	private Joueur joueur;
	
	public void propriete(int prix, Joueur joueur) {
		this.prix = prix;
		this.joueur = joueur;
	}

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
