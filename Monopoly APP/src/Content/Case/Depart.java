package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Depart implements Case {
	private Joueur joueur; 
	private int id;
	private int pactole;
	private String nom;
	
	public Depart(String nom, int id, int pactole) {
		this.nom = nom;
		this.pactole = pactole;
	}
	
	public void pactole(Joueur joueur) {
		joueur.setArgent(joueur.getArgent()+Configuration.getInstance().getPactole());
	}
	
	

}
