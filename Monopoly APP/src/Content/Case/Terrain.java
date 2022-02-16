package Content.Case;

import java.util.ArrayList;

import Content.Configuration;
import Content.Joueur;
import ePapotage.PapotageEvent;

public class Terrain extends Propriete {
	private int nbBuildings;
	private ArrayList<Integer> listeLoyer;

	// Constructeur
	public Terrain(String nom,int id, int prix, Joueur j, int building, ArrayList<Integer> listeLoyer, int hypotheque, int groupe) {
		super(nom, id, prix, j, hypotheque);
		this.nbBuildings = building;
		this.listeLoyer = new ArrayList<Integer>();
	}

	// Getter & Setter
	public int getNbBuilding() {
		return nbBuildings;
	}

	public void setNbBuilding(int building) {
		this.nbBuildings = building;
	}

	public ArrayList<Integer> getListeLoyer() {
		return listeLoyer;
	}

	public void setLoyer() {
		for (Propriete prop : this.getJoueur().getProprietes()) {
			if (nbBuildings == 0) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[0];
			}
			if (nbBuildings == 1) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[1];
			}
			if (nbBuildings == 2) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[2];
			}
			if (nbBuildings == 3) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[3];
			}
			if (nbBuildings == 4) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[4];
			}
			if (nbBuildings == 5) {
				this.loyer = Configuration.getInstance().getLoyer(prop.getId())[5];
			}

		}

	}
}
