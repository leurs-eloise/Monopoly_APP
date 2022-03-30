package Content.Case;

public class Prison implements Case {
	private String nom;
	private int id;
	private int escape;
	private int des;

	public Prison(String nom, int id, int escape, int des) {
		this.nom = nom;
		this.id = id;
		this.escape = escape;
		this.des = des;
	}

	public String getNom() {
		return nom;
	}

	public int getEscape() {
		return escape;
	}

	public int getDes() {
		return des;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return this.getClass().getName();
	}
}
