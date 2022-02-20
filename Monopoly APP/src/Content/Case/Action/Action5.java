package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action5 implements carteAction{

	private String description = "Payer 15$ pour excès de vitesse";
    private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action5() {
		Partie.getInstance().addAction(this);
	}
	
	// Getter & Setter
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setArgent(Partie.getInstance().getCurrentPlayer().getArgent()-15); // Amende pour exces de vitesse : payez 15$
		
	}
    
  
}
	
