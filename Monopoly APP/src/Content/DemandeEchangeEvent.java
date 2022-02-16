package Content;

import java.util.EventObject;
import Content.Case.Propriete;

public class DemandeEchangeEvent extends EventObject {
	private Propriete prop;
	private boolean choix;
	private int prix;

	public DemandeEchangeEvent(Object source, Propriete prop,boolean choix, int prix) {
		super(source);
		this.prix = prix;
		this.prop = prop;
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