package Content.Case;

import java.util.ArrayList;

public class CaseFactory {
	public Case getCase(ArrayList<String> caseInfo) {
		if(caseInfo.get(0) == null) {
			return null;
		}
		if(caseInfo.get(0).equalsIgnoreCase("prison")) {
			return new Prison();
		} else if(caseInfo.get(0).equalsIgnoreCase("carte")) {
			return new CaseCarte();
		} else if(caseInfo.get(0).equalsIgnoreCase("depart")) {
			return new Depart();
		} else if(caseInfo.get(0).equalsIgnoreCase("propriete")) {
			return new Propriete();
		} else if(caseInfo.get(0).equalsIgnoreCase("sansAction")) {
			return new SansAction();
		}
		return null;
	}
}
