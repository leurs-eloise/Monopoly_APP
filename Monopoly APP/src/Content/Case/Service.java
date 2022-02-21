package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Service extends Propriete {
	@SuppressWarnings("unused")
	private String nom;
	private int id;
	
	public Service(String nom, int id, int prix, Joueur j, int hypotheque) {
		super(nom, id, prix, j, hypotheque);
	}

	public int getNiveau() {
		int nbService = 0;
		for (Propriete prop : this.getJoueur().getProprietes()) {
			if (prop.getClass().getSimpleName() == "Service") {
				nbService += 1;
			}
		}
		if (nbService > 0) {
			return Configuration.getInstance().getMultiplicateur(this.id)[nbService-1];
		}
		return 0;
	}
	
	

}
