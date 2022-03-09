package Content.Case;
// Enlever id, nom et action
public class CaseCarte implements Case {
	
	public String nom;
    public int id;

    // Constructeur
    public CaseCarte(String nom, int id) {
        this.nom = nom;
        this.id = id;
    }
    
    // Getter & Setter
    public void setNom (String nom){
    	this.nom = nom;
    }
    
    public int getId() {
		return id;
	}
    @Override
    public String getNom() {
    	return nom;
    }
    public String getType() {
    	return this.getClass().getName();
    }

    
}
