package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action4 implements carteAction{

	private String description = "Rendez vous à Annecy";
    @SuppressWarnings("unused")
	private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action4() {
		Partie.getInstance().addAction(this);
	}
	
	// Getter & Setter
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setPosition(19); //Rendez-vous Annecy
		Partie.getInstance().actualiserPosition();
	}
    
  
}
	
