package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action7 implements carteAction{

	private String description = "Payer 50 polypoints";
    @SuppressWarnings("unused")
	private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action7() {
		Partie.getInstance().addAction(this);
	}
	
	// Getter & Setter
	public String getDescription() {
		return description;
	}


	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setArgent(Partie.getInstance().getCurrentPlayer().getArgent()-50); // Frais, payez 50$ 
		
	}
    
  
}
	
