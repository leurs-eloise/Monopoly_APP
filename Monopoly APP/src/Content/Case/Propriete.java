package Content.Case;

import Content.Joueur;

public class Propriete implements Case {
	private int id;
	private int prix;
	private int prixHypotheque;
	private Joueur joueur;
	private String nom;
	private boolean hypotheque;

	// Constructeur
	public Propriete(String nom, int id, int prix, Joueur j, int prixHypotheque) {
		this.nom = nom;
		this.id = id;
		this.prix = prix;
		this.joueur = null;
		this.hypotheque = false;
	}

	// Getter & Setter
	public int getPrixHypotheque() {
		return prixHypotheque;
	}

	public void setPrixHypotheque(int prixHypotheque) {
		this.prixHypotheque = prixHypotheque;
	}

	public boolean isHypotheque() {
		return hypotheque;
	}

	public void setHypotheque(boolean hypotheque) {
		this.hypotheque = hypotheque;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

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
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public String getType() {
		return this.getClass().getName();
	}

}
