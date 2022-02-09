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
	
}