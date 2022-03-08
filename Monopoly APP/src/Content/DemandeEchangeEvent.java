package Content;

import java.util.EventObject;
import Content.Case.Propriete;

public class DemandeEchangeEvent extends EventObject {
	private Propriete prop1;
	private Propriete prop2;
	private boolean choix;
	private int prix;

	public DemandeEchangeEvent(Object source, Propriete prop1, Propriete prop2,boolean choix, int prix) {
		super(source);
		this.prix = prix;
		this.prop1 = prop1;
		this.prop2 = prop2;
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

	public void setChoix(boolean choix) {
		this.choix = choix;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}
	
}