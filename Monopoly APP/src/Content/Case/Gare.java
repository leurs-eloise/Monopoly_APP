package Content.Case;

import java.util.ArrayList;

public class Gare extends Propriete{
	private int prix;
	private ArrayList<Integer> loyer = new ArrayList<>();
	
	public Gare(int prix, ArrayList<Integer> loyer) {
		super();
		this.prix = prix;
		this.loyer = loyer;
	}

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}

	public ArrayList<Integer> getLoyer() {
		return loyer;
	}

	public void setLoyer(ArrayList<Integer> loyer) {
		this.loyer = loyer;
	}
	
<<<<<<< HEAD
	
=======
>>>>>>> master

}
