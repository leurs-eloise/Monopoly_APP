package Content.Case.Action;

import Content.Joueur;

public class Action1 implements carteAction{

	public String nom;
    public int id;
    private Joueur joueur; 
    
	@Override
	public carteAction doAction() {
		// TODO Auto-generated method stub
		return null;
	}

	
	  // Getter & Setter
    public void setNom (String nom){
    	this.nom = nom;
    }
    
    public int getId() {
		return id;
	}
    
    public void Actions(Joueur joueur){
		cartes=new String[7];
		cartes[0]= joueur.setPosition(joueur.getPosition()-3); //Reculer de 3 cases 
		cartes[1]= joueur.setPosition(0); //Avancer case départ
		cartes[2]= joueur.setPosition(6); //Aller en prison
		cartes[3]= joueur.setPosition(20); //Rendez-vous Annecy
		cartes[4]= joueur.setArgent(joueur.getArgent()-15); // Amende pour excès de vitesse : payez 15$
		cartes[5]= joueur.setArgent(joueur.getArgent()+50); //La banque vous verse 50$
		cartes[6]= joueur.setArgent(joueur.getArgent()-50); // Frais, payez 50$ 
	}
    
    
}
joueur.setPosition(joueur.getPosition()-3);
	
