package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Service extends Propriete {
	@SuppressWarnings("unused")
	private String nom;

	public Service(String nom, int id, int prix, Joueur j, int hypotheque) {
		super(nom, id, prix, j, hypotheque);
	}

	public int getNiveau() {
		int nbService = getLevel();
		if (nbService > 0) {
			return Configuration.getInstance().getMultiplicateur(super.getId()).get(nbService - 1);
		}
		return 0;
	}
	public int getLevel() {
		int nbService = 0;
		for (Propriete prop : super.getJoueur().getProprietes()) {
			if (prop instanceof Service) {
				nbService += 1;
			}
		}
		return nbService;
	}

}
