package Content.Case.Action;

import Content.Joueur;
import Content.Partie;

public class Action7 implements carteAction{

	public String nom;
	private String action;
    public int id;
    private Joueur joueur =  Partie.getInstance().getCurrentPlayer(); 
    
    
    // Constructeur
	public Action7(String nom, String description) {
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
		joueur.setArgent(joueur.getArgent()-50); // Frais, payez 50$ 
		
	}
    
  
}
	
