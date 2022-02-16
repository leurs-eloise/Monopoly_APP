package Content.Case;
// Enlever id, nom et action
public class CaseCarte implements Case {
<<<<<<< HEAD
	
	public String nom;
    public int id;
    public String action;
    
    // Constructeur
    public CaseCarte(String nom, int id, String action) {
        this.nom = nom;
        this.id = id;
        this.action = action;
    }
    
    // Getter & Setter
    public void setNom (String nom){
    	this.nom = nom;
    }
    
    public int getId() {
		return id;
	}
    
    public String getAction() {
		return action;
	}

    
=======
  
    private String nom;
    private String action;
    private int id;
    public CaseCarte(String nom, int id) {
    	this.nom = nom;
    	this.id = id;
    }
    
>>>>>>> master
}
