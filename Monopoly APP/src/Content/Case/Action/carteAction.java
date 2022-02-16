package Content.CaseCarte;

public interface carteAction implements CaseCarte {
	
	private String nom;
	private String action;
	private int id;
	
	// Constructeur
		public carteAction(String nom, String action, int id) {
			this.nom = nom;
			this.action = action;
			this.id = id;
		}
		
		// Getter & Setter		
		public int getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}
		
				
		public int getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
	
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	
}
