package Content;

import java.util.ArrayList;
import java.util.Objects;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	
	private ArrayList<String> case1 = new ArrayList<String>(
			
			);
			
			
	private static Configuration configuration;
	public Configuration() {}
	
	public Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}
}
