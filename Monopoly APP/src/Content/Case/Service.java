package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Service extends Propriete {
	private int multiplicateur;
	private String nom;
	
	public Service(String nom, int id, int prix, Joueur j, int hypotheque, int multiplicateur) {
		super(nom, id, prix, j, hypotheque);
		this.multiplicateur = multiplicateur;
	}

	public int getNiveau() {
		
		for(Propriete prop : this.getJoueur().getProprietes()) {
			if ((prop.getId() == 3) && (prop.getId() == 13)){
				this.multiplicateur = Configuration.getInstance().getMultiplicateur(3)[1];
			}
			else if ((prop.getId() == 3) || (prop.getId() == 13)) {
				this.multiplicateur = Configuration.getInstance().getMultiplicateur(3)[0];
			}
		}
		return this.multiplicateur;
	}
	

}
