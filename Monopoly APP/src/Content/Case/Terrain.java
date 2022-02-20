package Content.Case;

import java.util.ArrayList;

import Content.Configuration;
import Content.Joueur;

public class Terrain extends Propriete {
	private int nbBuildings;
	private ArrayList<Integer> listeLoyer = new ArrayList<Integer>();

	// Constructeur
	public Terrain(String nom,int id, int prix, Joueur j, int building, ArrayList<Integer> listeLoyer, int hypotheque, int groupe) {
		super(nom, id, prix, j, hypotheque);
		this.nbBuildings = building;
		this.listeLoyer = listeLoyer;
	}

	// Getter & Setter
	public int getNbBuilding() {
		return nbBuildings;
	}

	public void setNbBuilding(int building) {
		this.nbBuildings += building;
	}

	public ArrayList<Integer> getListeLoyer() {
		return listeLoyer;
	}

}
