package Content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	
	private ArrayList<ArrayList<Objects>> listeCase = new ArrayList<ArrayList<Objects>>();

	
			
	private static Configuration configuration;
	public Configuration() {}
	
	public static Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}
	public ArrayList<ArrayList<Objects>> getListeCase(){
		return listeCase;
	}
	public void loadDefaultConfig() throws IOException, JSONException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("defaultConfig.json")));
		String inputLine;
		String response = "";

		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONObject test = new JSONObject(response);
		test.get("Data");
		System.out.println(test);
		
	}
	public String getName(int id) {
		return listeCase.get(id).get(0).toString();
	}

	public int[] getLoyer(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getMultiplicateur(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
