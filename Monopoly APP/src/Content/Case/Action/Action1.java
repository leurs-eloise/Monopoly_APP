package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action1 implements carteAction{

	public String nom;
	private String action;
    public int id;
    private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action1(String nom, String description) {
	}
	
	// Getter & Setter
    public void setNom (String nom){
    	this.nom = nom;
    }
    
    public int getId() {
		return id;
	}


	@Override
	public void doAction() {
		joueur.setPosition(joueur.getPosition()-3); //Reculer de 3 cases
		
	}
    
  
}
	
