package Content.Case;

import java.util.ArrayList;

public class Terrain extends Propriete  {
	private int building;
	private ArrayList<Integer> loyer = new ArrayList<>();
	
	// Constructeur
	public Terrain(int building, ArrayList<Integer> loyer) {
		super();
		this.building = building;
		this.loyer = loyer;
	}
	// Getter & Setter
	public int getBuilding() {
		return building;
	}

	public void setBuilding(int building) {
		this.building = building;
	}

	public ArrayList<Integer> getLoyer() {
		return loyer;
	}

	public void setLoyer(ArrayList<Integer> loyer) {
		this.loyer = loyer;
	}
	
	
	
	

}
