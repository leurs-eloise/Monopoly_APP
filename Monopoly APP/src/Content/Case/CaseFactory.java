package Content.Case;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;


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
			return new Prison(nom, id, caseInfo.getInt("amountToEscape"), caseInfo.getInt("diceValue"));
		} else if(type.equalsIgnoreCase("caseCarte")) {//FAIT
			return new CaseCarte(nom, id);
		} else if(type.equalsIgnoreCase("depart")) { //FAIT
			return new Depart(nom, id, caseInfo.getInt("gain"));
		} else if(type.equalsIgnoreCase("terrain")) {
			return new Terrain(nom, id, caseInfo.getInt("prix"),null, 0, JSONArrayToIntArrayList(caseInfo.getJSONArray("loyer")), caseInfo.getInt("hypoteque"), caseInfo.getInt("groupe"));
		} else if(type.equalsIgnoreCase("sansAction")) {
			return new SansAction(nom, id);
		} else if(type.equalsIgnoreCase("service")) {
			return new Service(nom, id, caseInfo.getInt("prix"), null, caseInfo.getInt("hypoteque"));
		} else if(type.equalsIgnoreCase("gare")) {
			return new Gare(nom, id, caseInfo.getInt("prix"), null, caseInfo.getInt("hypoteque"));
		}else if(type.equalsIgnoreCase("enPrison")) {
			return new EnPrison(nom, id);
		}
		return null;
	}
	public ArrayList<Integer> JSONArrayToIntArrayList(JSONArray array){
		ArrayList<Integer> list = new ArrayList<Integer>();
		String JSONString = array.toString();
		JSONString =  (String) JSONString.subSequence(1, JSONString.length()-1);
		String[] StringSplit = JSONString.split(",");
		for(String str : StringSplit) {
			list.add(Integer.parseInt(str));
		}
		return list;
	}
}
