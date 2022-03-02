package Content;

import java.util.EventObject;

import Content.Case.Propriete;

public class EchangeEvent extends EventObject {
	private Joueur joueur;
	private boolean choix;
	private Propriete prop1;
	private Propriete prop2;
	private int prix;
	
	public EchangeEvent(Object source, Joueur joueur, boolean choix) {
		super(source);
		this.setJoueur(joueur);
		this.choix = choix;
	
	}
	
	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public void setChoix(boolean choix) {
		this.choix = choix;
	}

	public Propriete getProp1() {
		return prop1;
	}

	public void setProp1(Propriete prop1) {
		this.prop1 = prop1;
	}

	public Propriete getProp2() {
		return prop2;
	}

	public void setProp2(Propriete prop2) {
		this.prop2 = prop2;
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
