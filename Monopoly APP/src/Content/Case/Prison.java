package Content.Case;

public class Prison implements Case {
	private String nom;
	private int id;
	public Prison(String nom, int id) {
		this.nom = nom;
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
}
