package Content.Case;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONObject;

import Content.Configuration;

public class CaseFactory {
	private static CaseFactory caseFactory;
	
	public CaseFactory() {}
	
	public static CaseFactory getInstance() {
		if(Objects.isNull(caseFactory)) {
			caseFactory = new CaseFactory();
		}
		return caseFactory;
	}
	
	public Case createCase(String type, JSONObject caseInfo,int id) {
		String nom = caseInfo.getString("nom");
		if(type == null) {
			return null;
		}
		if(type.equalsIgnoreCase("prison")) {
			return new Prison(nom, id);
		} else if(type.equalsIgnoreCase("caseCarte")) {//FAIT
			return new CaseCarte(nom, id);
		} else if(type.equalsIgnoreCase("depart")) { //FAIT
			return new Depart(nom, id, caseInfo.getInt("gain"));
		} else if(type.equalsIgnoreCase("terrain")) {
			return new Terrain(nom, id, 0,null, 0, 0);
		} else if(type.equalsIgnoreCase("sansAction")) {
			return new SansAction(nom, id);
		} else if(type.equalsIgnoreCase("service")) {
			return new Service(nom, id, 0, null, 0);
		} else if(type.equalsIgnoreCase("gare")) {
			return new Gare(nom, id, 0, null);
		}
		return null;
	}
}
