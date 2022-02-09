package Content;

import java.util.EventObject;

public class EchangeEvent extends EventObject {
	private Joueur joueur;
	
	public EchangeEvent(Object source, Joueur joueur ) {
		super(source);
		this.setJoueur(joueur);
	
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	 
}
