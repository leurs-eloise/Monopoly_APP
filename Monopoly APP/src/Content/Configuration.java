package Content;

import java.util.ArrayList;
import java.util.Objects;

import Content.Case.Case;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	
	private ArrayList<Case> listeCase = new ArrayList<Case>();

	
			
	private static Configuration configuration;
	public Configuration() {}
	
	public Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}
	public ArrayList<Case> getListeCase(){
		return listeCase;
	}
	
}
