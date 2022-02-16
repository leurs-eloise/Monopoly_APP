package Content.Case.Action;

import Content.Joueur;

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
		joueur.setArgent(joueur.getArgent()-15); // Amende pour exces de vitesse : payez 15$
		
	}
    
  
}
	
