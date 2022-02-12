package Content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import Content.Case.CaseFactory;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	
	private ArrayList<JSONObject> listeCase = new ArrayList<JSONObject>();

	
			
	private static Configuration configuration;
	public Configuration() {}
	
	public static Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}
	
	
	public String loadDefaultConfig() throws IOException, JSONException {
		
		// CONVERTION DU FICHIER DE CONFIG EN JSON
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("defaultConfig.json")));
		String inputLine;
		String response = "";

		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONObject configJSON = new JSONObject(response);
		
		
		// LECTURE ET TRAITEMENT DU FICHIER
		JSONObject gameSetting = configJSON.getJSONObject("GameSetting");
		JSONObject caseListInformation = configJSON.getJSONObject("Case");
		try {
			nbCase = gameSetting.getInt("case");
			nbJoueur = gameSetting.getInt("player");
		}catch (Exception e) {
			System.out.println("[Erreur] Paramètre case et/ou joueur non renseigné. Tentative prise de valeur par défaut (20 cases et 2 joueurs)");
		}
		try{
			caseListInformation.get(Integer.toString(nbCase-1));
		}catch (Exception e) {
			System.out.println("[Erreur] Nombre de case insuffisant.");
			return null;
		}
		for(int i=0; i<nbCase; i++) {
			boolean depart = false;
			String erreur = "";
			ArrayList<JSONObject> listeGare = new ArrayList<JSONObject>();
			JSONObject caseInfo = null;
			try {
				caseInfo= caseListInformation.getJSONObject(Integer.toString(i));
			}catch (Exception e) {
				System.out.println("[Erreur] Case " + i + " introuvable.");
				return null;
			}
			try {
				String type = caseInfo.getString("type");
				if(type.equals("depart")) {
					if(i == 0) {
						if(!depart) {
							try {
								caseInfo.getString("nom");
								caseInfo.getInt("gain");
								listeCase.add(caseInfo);
								caseInfo.remove("type");
								CaseFactory.getInstance().createCase(type, caseInfo, i);
								depart = true;
							}catch (Exception e) {
								erreur+="\n[Erreur] Propriété case départ non défini (nom/gain)";
							}
						} else {
							erreur+="\n[Erreur] Le plateau doit comporter qu'une seul case départ";
						}
					} else {
						erreur+="\n[Erreur] La case départ doit avoir comme indice 0";
					}
					
				} else if(type.equals("caseCarte")) {
					try {
						caseInfo.getString("nom");
						listeCase.add(caseInfo);
						caseInfo.remove("type");
						CaseFactory.getInstance().createCase(type, caseInfo, i);
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else if(type.equals("gare")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("prix");
						caseInfo.getJSONArray("loyer");
						listeGare.add(caseInfo);
						listeCase.add(caseInfo);
						caseInfo.remove("type");
						CaseFactory.getInstance().createCase(type, caseInfo, i);
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom/prix/loyer)";
					}

				} else if(type.equals("terrain")) {
					
				} else if(type.equals("sansAction")) {
					
				} else if(type.equals("prison")) {
					
				} else if(type.equals("service")) {
					
				}
				System.out.println(erreur);
			}catch (Exception e) {
				System.out.println("[Erreur] Case " + i + ": type non renseigné.");
				return null;
			}
			
		}
		return null;


		
	}
	public String getName(int id) {
		return null;
	}

	public int[] getLoyer(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getMultiplicateur(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPactole() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
