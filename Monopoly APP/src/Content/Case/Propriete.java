package Content.Case;


import Content.Joueur;

public class Propriete implements Case {
	private int id;
	private int prix;
	private Joueur joueur;
	private String nom;
	
	// Constructeur
	public Propriete(String nom, int id, int prix, Joueur j, int hypotheque) {
		this.nom = nom;
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

}
