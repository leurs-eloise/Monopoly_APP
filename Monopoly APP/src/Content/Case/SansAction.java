package Content.Case;

public class SansAction implements Case{
	private String nom;
	@SuppressWarnings("unused")
	private int id;
	public SansAction(String nom, int id) {
		this.nom = nom;
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
}
