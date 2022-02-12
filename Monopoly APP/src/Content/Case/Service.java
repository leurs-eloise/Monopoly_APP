package Content.Case;

import Content.Configuration;
import Content.Joueur;

public class Service extends Propriete {
	private int multiplicateur;
	private String nom;
	
	public Service(String nom, int id, int prix, Joueur j, int multiplicateur) {
		super(nom, id, prix, j);
		this.multiplicateur = multiplicateur;
	}

	public int getNiveau() {
		return this.multiplicateur;
	}

	public void setMultiplicateur() {
		//service id1 = 3 et 13
		for(Propriete prop : this.getJoueur().getProprietes()) {
			if ((prop.getId() == 3) && (prop.getId() == 13)){
				this.multiplicateur = Configuration.getInstance().getMultiplicateur(3)[1];
			}
			else if ((prop.getId() == 3) || (prop.getId() == 13)) {
				this.multiplicateur = Configuration.getInstance().getMultiplicateur(3)[0];
			}
		}
	}
	
	
	

}
