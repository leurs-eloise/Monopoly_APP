package Content.Case;

public class SansAction implements Case{
	private String nom;
	private int id;
	public SansAction(String nom, int id) {
		this.nom = nom;
	}
	public String getNom() {
		return nom;
	}
}
