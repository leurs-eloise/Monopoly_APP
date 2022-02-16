package Content.Case;

import java.util.ArrayList;

import Content.Configuration;
import Content.Joueur;

public class Gare extends Propriete {
	private int id;
	
	public Gare(String nom,int id, int prix, Joueur j, int hypotheque) {
		super(nom, id, prix, j, hypotheque);
	}

	public int getPrix() {
		return this.getPrix();
	}

	public int getLoyer() {
		int nbGare = 0;
		for (Propriete prop : this.getJoueur().getProprietes()) {
			if (prop.getClass().getSimpleName() == "Gare") {
				nbGare += 1;
			}
		}
		if (nbGare > 0) {
			return Configuration.getInstance().getLoyer(this.id)[nbGare-1];
		}
		return 0;
	}

}
