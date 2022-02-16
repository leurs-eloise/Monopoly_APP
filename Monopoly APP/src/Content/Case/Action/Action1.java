package Content.Case.Action;

import Content.Joueur;

public class Action1 implements carteAction{

	public String nom;
	private String action;
    public int id;
    private Joueur joueur; 
    
    
    // Constructeur
	public Action1(String nom, String action, int id, Joueur joueur) {
		super(nom, action, id, joueur);
	}

	
	
	// Getter & Setter
    public void setNom (String nom){
    	this.nom = nom;
    }
    
    public int getId() {
		return id;
	}
    
    public void Actions(Joueur joueur){
    	
		String[] cartes=new String[7];
		cartes[0]= joueur.setPosition(joueur.getPosition()-3); //Reculer de 3 cases 
		cartes[1]= joueur.setPosition(0); //Avancer case depart
		cartes[2]= joueur.setPosition(6); //Aller en prison
		cartes[3]= joueur.setPosition(20); //Rendez-vous Annecy
		cartes[4]= joueur.setArgent(joueur.getArgent()-15); // Amende pour exces de vitesse : payez 15$
		cartes[5]= joueur.setArgent(joueur.getArgent()+50); //La banque vous verse 50$
		cartes[6]= joueur.setArgent(joueur.getArgent()-50); // Frais, payez 50$ 
		
	}
    
  
}
	
