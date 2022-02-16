package Content.Case;

import java.util.ArrayList;

import Content.Configuration;
import Content.Joueur;

public class Gare extends Propriete {
	private int loyer;

	public Gare(String nom,int id, int prix, Joueur j, int hypotheque) {
		super(nom, id, prix, j, hypotheque);
	}

	public int getPrix() {
		return this.getPrix();
	}

	public int getLoyer() {
		int nbGare = 0;
		for (Propriete prop : this.getJoueur().getProprietes()) {
			if ((prop.getId() == 2) || (prop.getId() == 7) || (prop.getId() == 12) || (prop.getId() == 17)) {
				nbGare += 1;
			}
		}
		if (nbGare == 4) {
			this.loyer = Configuration.getInstance().getLoyer(2)[3];
		} else if (nbGare == 3) {
			this.loyer = Configuration.getInstance().getLoyer(2)[2];
		} else if (nbGare == 2) {
			this.loyer = Configuration.getInstance().getLoyer(2)[1];
		} else if (nbGare == 1) {
			this.loyer = Configuration.getInstance().getLoyer(2)[0];
		}	

		return this.loyer;
	}

}
