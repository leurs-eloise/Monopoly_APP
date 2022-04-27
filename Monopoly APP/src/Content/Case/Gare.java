package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Gare extends Propriete {

	public Gare(String nom, int id, int prix, Joueur j, int hypotheque) {
		super(nom, id, prix, j, hypotheque);
	}

	public int getLoyer() {
		int nbGare = getLevel();
		if (nbGare > 0) {
			return Configuration.getInstance().getLoyer(super.getId()).get(nbGare - 1);
		}
		return 0;
	}
	public int getLevel() {
		int nbGare = 0;
		for (Propriete prop : super.getJoueur().getProprietes()) {
			if (prop instanceof Gare) {
				nbGare += 1;
			}
		}
		return nbGare;
	}

}
