package Content.Case;

import java.util.ArrayList;

import Content.Configuration;
import Content.Joueur;

public class Terrain extends Propriete  {
	private int nbBuildings;
	private int loyer;
	
	// Constructeur
	public Terrain(int id, int prix, Joueur j, int building, int loyer) {
		super(id, prix, j);
		this.nbBuildings = building;
		this.loyer = loyer;
	}
	// Getter & Setter
	public int getNbBuilding() {
		return nbBuildings;
	}

	public void setNbBuilding(int building) {
		this.nbBuildings = building;
	}

	public int getLoyer() {
		return loyer;
	}

	public void setLoyer() {
		for(Propriete prop : this.getJoueur().getProprietes()) {
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
