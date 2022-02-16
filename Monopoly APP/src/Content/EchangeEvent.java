package Content;

import java.util.EventObject;

import Content.Case.Propriete;

public class EchangeEvent extends EventObject {
	private Joueur joueur;
	private boolean choix;
	private Propriete prop;
	
	public EchangeEvent(Object source, Joueur joueur, boolean choix ) {
		super(source);
		this.setJoueur(joueur);
		this.choix = choix;
	
	}
	
	
	
	public Propriete getProp() {
		return prop;
	}


	public void setProp(Propriete prop) {
		this.prop = prop;
	}



	public boolean isChoix() {
		return choix;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	 
}
