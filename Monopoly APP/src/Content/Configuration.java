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

import Content.Case.Case;
import Content.Case.CaseFactory;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	private int lvlMaxProp = 6;
	
	private ArrayList<JSONObject> listeCaseJSON = new ArrayList<JSONObject>();
	private ArrayList<Case> listeCase = new ArrayList<Case>();

	
			
	private static Configuration configuration;
	public Configuration() {}
	
	public static Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}
	
	
	public String loadDefaultConfig() throws IOException, JSONException {
		System.out.println("[Info] Lecture configuration ...");
		listeCaseJSON.clear();
		// CONVERTION DU FICHIER DE CONFIG EN JSON
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("defaultConfig.json")));
		String inputLine;
		String response = "";

		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONObject configJSON = new JSONObject(response);
		if(Objects.isNull(configJSON)) {
			System.out.println("[Info] Erreur lors du chargement de la configuration");
			return null;
		}
		
		// LECTURE ET TRAITEMENT DU FICHIER
		JSONObject gameSetting = configJSON.getJSONObject("GameSetting");
		JSONObject caseListInformation = configJSON.getJSONObject("Case");
		try {
			nbCase = gameSetting.getInt("case");
			nbJoueur = gameSetting.getInt("player");
			lvlMaxProp = gameSetting.getInt("niveauTerrain");
		}catch (Exception e) {
			System.out.println("[Erreur] Paramètre case/joueur/level mal renseigné. Tentative prise de valeur par défaut (20 cases et 2 joueurs)");
		}
		try{
			caseListInformation.get(Integer.toString(nbCase-1));
		}catch (Exception e) {
			System.out.println("[Erreur] Nombre de cases insuffisant.");
			return null;
		}
		ArrayList<JSONObject> listeGare = new ArrayList<JSONObject>();
		ArrayList<JSONObject> listeService = new ArrayList<JSONObject>();
		ArrayList<JSONObject> listeTerrain = new ArrayList<JSONObject>();
		// CREATION DES CASES
		String erreur = "";
		boolean depart = false;
		boolean prison = false;
		System.out.println("[Info] Vérification des cases ...");
		for(int i=0; i<nbCase; i++) {


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
					if(!depart) {
						if(i == 0) {
							try {
								caseInfo.getString("nom");
								caseInfo.getInt("gain");
								listeCaseJSON.add(caseInfo);
								caseInfo.remove("type");
								listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
								depart = true;
							}catch (Exception e) {
								erreur+="\n[Erreur] Propriété case départ non défini (nom/gain)";
							}
						} else {
							erreur+="\n[Erreur] La case départ doit avoir comme indice 0";
						}
					} else {
						erreur+="\n[Erreur] Le plateau doit comporter qu'une seul case départ";
					}
					
				} else if(type.equals("caseCarte")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else if(type.equals("gare")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("prix");
						caseInfo.getJSONArray("loyer");
						caseInfo.getInt("hypoteque");
						listeGare.add(caseInfo);
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom/prix/loyer/hypoteque)";
					}

				} else if(type.equals("terrain")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("groupe");
						caseInfo.getInt("prix");
						caseInfo.getInt("prixConstruction");
						caseInfo.getJSONArray("loyer");
						caseInfo.getInt("hypoteque");
						if(caseInfo.getJSONArray("loyer").length() < lvlMaxProp) {
							erreur+="\n[Erreur] "+ lvlMaxProp + " prix sont exigés pour le loyer sur la case " + i;
						}
						listeTerrain.add(caseInfo);
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom/groupe/prix/prixConstruction/loyer/hypoteque)";
					}
					
				} else if(type.equals("sansAction")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else if(type.equals("prison")) {
					try {
						if(!prison) {
							caseInfo.getString("nom");
							listeCaseJSON.add(caseInfo);
							caseInfo.remove("type");
							listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
							prison =true;
						} else {
							erreur +="[Erreur] Le plateau doit comporter qu'une seul case prison";
						}
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else if(type.equals("service")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("prix");
						caseInfo.getJSONArray("multiplicateur");
						caseInfo.getInt("hypoteque");
						listeCaseJSON.add(caseInfo);
						listeService.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
				} else if(type.equals("enPrison")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else {
					erreur += "\n[Erreur] Type de la case n°" + i + " non valide";
				}

			}catch (Exception e) {
				System.out.println("[Erreur] Case " + i + ": type non renseigné.");
				return null;
			}

			
		}
		if(!erreur.equals("")) {
			System.out.println(erreur);
			return null;
		}
		
		// DERNIERE VERIF
		System.out.println("[Info] Vérification propriétés ...");
		boolean erreurBoolean = false;
		int nbGare = listeGare.size();
		for(JSONObject caseInfo : listeGare) { //verif loyer gare
			if(caseInfo.getJSONArray("loyer").length() < nbGare) {
				System.out.println("[Erreur] Gare " + caseInfo.getString("nom") + " doit avoir au moins " + nbGare + " loyer.");
				erreurBoolean = true;
			}
		}
		int nbService = listeService.size();
		for(JSONObject caseInfo : listeService) { //verif loyer gare
			if(caseInfo.getJSONArray("multiplicateur").length() < nbService) {
				System.out.println("[Erreur] Service " + caseInfo.getString("nom") + " doit avoir au moins " + nbService + " multiplicateur.");
				erreurBoolean = true;
			}
		}
		int groupe = -1;
		for(JSONObject caseInfo : listeTerrain) {
			if(caseInfo.getInt("groupe") > groupe +1) {
				System.out.println("[Erreur] Le groupe de "+ caseInfo.getString("nom") + " est invalide. (" + (groupe+1) + " au max est attendu)");
				erreurBoolean = true;
			} else {
				if(caseInfo.getInt("groupe")>= groupe) {
					groupe = caseInfo.getInt("groupe");
				}
			}
		}
		
		
		if(!erreurBoolean) {
			System.out.println("[Info] Configuration par défaut chargé avec succès !");
			System.out.println("[Info] Plateau de " + listeCase.size() + " case(s) créé avec succès !");
		} else {
			return null;
		}
		
		return "OK";

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
