package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action1 implements carteAction{

	private String description = "Reculer de 3 cases";
    private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action1() {
		Partie.getInstance().addAction(this);
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setPosition(Partie.getInstance().getCurrentPlayer().getPosition()-3); //Reculer de 3 cases
		Partie.getInstance().actualiserPosition();
		
	}
    
  
}
	
