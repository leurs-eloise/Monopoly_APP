package Content.Case;

public class EnPrison implements Case {
	private String nom;
	private int id;
	public EnPrison(String nom, int id) {
		this.nom = nom;
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
}
