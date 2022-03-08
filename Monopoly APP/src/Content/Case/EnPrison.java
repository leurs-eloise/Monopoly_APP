package Content.Case;

public class EnPrison implements Case {
	private String nom;
	@SuppressWarnings("unused")
	private int id;
	public EnPrison(String nom, int id) {
		this.nom = nom;
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public String getType() {
    	return this.getClass().getName();
    }
}
