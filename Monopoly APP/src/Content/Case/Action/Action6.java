package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action6 implements carteAction{

	private String description = "La banque vous verse 50 polypoints";
    @SuppressWarnings("unused")
	private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action6() {
		Partie.getInstance().addAction(this);
	}
	
	// Getter & Setter    
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setArgent(Partie.getInstance().getCurrentPlayer().getArgent()+50); //La banque vous verse 50$
		
	}
    
  
}
	
