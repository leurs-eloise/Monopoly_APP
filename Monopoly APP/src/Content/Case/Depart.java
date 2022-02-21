package Content.Case;

import Content.Joueur;

public class Depart implements Case {
	@SuppressWarnings("unused")
	private int id;
	private int pactole;
	private String nom;
	
	public Depart(String nom, int id, int pactole) {
		this.nom = nom;
		this.pactole = pactole;
		this.id = id;
	}
	
	public void pactole(Joueur joueur) {
		joueur.setArgent(joueur.getArgent()+pactole);
	}
	

	public String getNom() {
		return nom;
	}
	public int getPactole() {
		return pactole;
	}

}
