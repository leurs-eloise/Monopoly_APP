package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action3 implements carteAction{

	private String description = "Aller en prison";
    @SuppressWarnings("unused")
	private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action3() {
		Partie.getInstance().addAction(this);
	}
	
	// Getter & Setter
	public String getDescription() {
		return description;
	}


	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setPosition(6); //Aller en prison
		Partie.getInstance().actualiserPosition();
	}
    
  
}
	
