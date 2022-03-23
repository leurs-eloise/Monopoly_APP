package Content;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import Content.Case.Case;
import Content.Case.CaseFactory;
import Content.Case.Action.Action1;
import Content.Case.Action.Action2;
import Content.Case.Action.Action3;
import Content.Case.Action.Action4;
import Content.Case.Action.Action5;
import Content.Case.Action.Action6;
import Content.Case.Action.Action7;
import Server.SendString;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	private int lvlMaxProp = 6;
	public boolean configLoad = false;
	private int defaultMoney = 1500;
	private JSONObject playerConfig = null;

	private ArrayList<JSONObject> listeCaseJSON = new ArrayList<JSONObject>();
	private ArrayList<Case> listeCase = new ArrayList<Case>();
	private SendString stringToSend = SendString.getInstance();
	private boolean hypothequeEnable = false;
	private static Configuration configuration;

	public Configuration() {
	}

	public JSONObject getPlayerConfig() {
		return playerConfig;
	}
	
	public ArrayList<JSONObject> getListeCaseJSON() {
		return listeCaseJSON;
	}
	public static Configuration getInstance() {
		if (Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}

	public boolean loadConfig(String file) { //Config perso 
        try(BufferedReader br = new BufferedReader(new FileReader(file)))  
        { 
            String reponse  = ""; 
            String line =""; 
            while ((line = br.readLine()) != null) { 
            	reponse += line; 
            } 
            br.close(); 
    		JSONObject configJSON = new JSONObject(reponse); 
    		if (Objects.isNull(configJSON)) { 
    			stringToSend.receiveMsg("[Info] Erreur lors du chargement de la configuration"); 
    			return false; 
    		} 
    		// LECTURE ET TRAITEMENT DU FICHIER 
    		listeCase.clear();
    		listeCaseJSON.clear();
    		playerConfig = null;
    		configLoad = false;
    		try { 
    			playerConfig = configJSON.getJSONObject("Player"); 
    		}catch (Exception e) { 
    			stringToSend.receiveMsg("[Info] Parametre Player non renseigne."); 
			} 
 
    		 
    		JSONObject gameSetting = configJSON.getJSONObject("GameSetting"); 
    		JSONObject caseListInformation = configJSON.getJSONObject("Case"); 
    		try { 
    			nbCase = gameSetting.getInt("case"); 
 
    		} catch (Exception e) { 
    			stringToSend.receiveMsg("[Erreur] Parametre case mal renseigne. Tentative prise de valeur par defaut (20)"); 
    		} 
    		try { 
    			nbJoueur = gameSetting.getInt("player"); 
    		} catch (Exception e) { 
    			stringToSend 
    					.receiveMsg("[Erreur] Parametre player mal renseigne. Tentative prise de valeur par defaut (2)"); 
    		} 
    		try { 
    			lvlMaxProp = gameSetting.getInt("niveauTerrain"); 
    		} catch (Exception e) { 
    			stringToSend.receiveMsg( 
    					"[Erreur] Parametre niveauTerrain depart mal renseigne. Tentative prise de valeur par defaut (6)"); 
    		} 
    		try { 
    			hypothequeEnable = gameSetting.getBoolean("hypotheque"); 
    		} catch (Exception e) { 
    			stringToSend.receiveMsg( 
    					"[Erreur] Parametre hypotheque depart mal renseigne. Tentative prise de valeur par defaut (false)"); 
    		} 
    		try { 
    			defaultMoney = gameSetting.getInt("defaultMoney"); 
    		} catch (Exception e) { 
    			stringToSend.receiveMsg( 
    					"[Erreur] Parametre defaultMoney mal renseigne. Tentative prise de valeur par defaut (1500)"); 
    		} 
    		try { 
    			caseListInformation.get(Integer.toString(nbCase - 1)); 
    		} catch (Exception e) { 
    			stringToSend.receiveMsg("[Erreur] Nombre de cases insuffisant."); 
 
    			return false; 
 
    		} 
    		ArrayList<JSONObject> listeGare = new ArrayList<JSONObject>(); 
    		ArrayList<JSONObject> listeService = new ArrayList<JSONObject>(); 
    		ArrayList<JSONObject> listeTerrain = new ArrayList<JSONObject>(); 
    		// CREATION DES CASES 
    		String erreur = ""; 
    		boolean depart = false; 
    		boolean prison = false; 
    		boolean enPrison = false; 
    		stringToSend.receiveMsg("[Info] Verification des cases ..."); 
    		for (int i = 0; i < nbCase; i++) { 
 
    			JSONObject caseInfo = null; 
    			try { 
    				caseInfo = caseListInformation.getJSONObject(Integer.toString(i)); 
    			} catch (Exception e) { 
    				stringToSend.receiveMsg("[Erreur] Case " + i + " introuvable."); 
 
    				return false; 
 
    			} 
    			try { 
    				String type = caseInfo.getString("type"); 
    				if (type.equals("depart")) { 
    					if (!depart) { 
    						if (i == 0) { 
    							try { 
    								caseInfo.getString("nom"); 
    								caseInfo.getInt("gain"); 
    								listeCaseJSON.add(caseInfo); 
    								caseInfo.remove("type"); 
    								listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    								depart = true; 
    							} catch (Exception e) { 
    								erreur += "\n[Erreur] Propriete case depart non defini (nom/gain)"; 
    							} 
    						} else { 
    							erreur += "\n[Erreur] La case depart doit avoir comme indice 0"; 
    						} 
    					} else { 
    						erreur += "\n[Erreur] Le plateau doit comporter qu'une seul case depart"; 
    					} 
 
    				} else if (type.equals("caseCarte")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						listeCaseJSON.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type + " non defini (nom)"; 
    					} 
 
    				} else if (type.equals("gare")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						caseInfo.getInt("prix"); 
    						caseInfo.getJSONArray("loyer"); 
    						caseInfo.getInt("hypoteque"); 
    						listeGare.add(caseInfo); 
    						listeCaseJSON.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type 
    								+ " non defini (nom/prix/loyer/hypoteque)"; 
    					} 
 
    				} else if (type.equals("terrain")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						caseInfo.getInt("groupe"); 
    						caseInfo.getInt("prix"); 
    						caseInfo.getInt("prixConstruction"); 
    						caseInfo.getJSONArray("loyer"); 
    						caseInfo.getInt("hypoteque"); 
    						if (caseInfo.getJSONArray("loyer").length() < lvlMaxProp) { 
    							erreur += "\n[Erreur] " + lvlMaxProp + " prix sont exiges pour le loyer sur la case " + i; 
    						} 
    						listeTerrain.add(caseInfo); 
    						listeCaseJSON.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type 
    								+ " non defini (nom/groupe/prix/prixConstruction/loyer/hypoteque)"; 
    					} 
 
    				} else if (type.equals("sansAction")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						listeCaseJSON.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type + " non defini (nom)"; 
    					} 
 
    				} else if (type.equals("prison")) { 
    					try { 
    						if (!prison) { 
    							caseInfo.getString("nom"); 
    							caseInfo.getInt("amountToEscape"); 
    							caseInfo.getInt("diceValue"); 
    							listeCaseJSON.add(caseInfo); 
    							caseInfo.remove("type"); 
    							listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    							prison = true; 
    						} else { 
    							erreur += "[Erreur] Le plateau doit comporter qu'une seul case prison"; 
    						} 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type 
    								+ " non defini (nom/amountToEscape/diceValue)"; 
    					} 
 
    				} else if (type.equals("service")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						caseInfo.getInt("prix"); 
    						caseInfo.getJSONArray("multiplicateur"); 
    						caseInfo.getInt("hypoteque"); 
    						listeCaseJSON.add(caseInfo); 
    						listeService.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type + " non defini (nom)"; 
    					} 
    				} else if (type.equals("enPrison")) { 
    					try { 
    						caseInfo.getString("nom"); 
    						listeCaseJSON.add(caseInfo); 
    						caseInfo.remove("type"); 
    						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i)); 
    						enPrison = true; 
    					} catch (Exception e) { 
    						erreur += "\n[Erreur] Propriete case ne" + i + " de type " + type + " non defini (nom)"; 
    					} 
 
    				} else { 
    					erreur += "\n[Erreur] Type de la case ne" + i + " non valide"; 
    				} 
 
    			} catch (Exception e) { 
    				stringToSend.receiveMsg("[Erreur] Case " + i + ": type non renseigne."); 
 
    				return false; 
 
    			} 
 
    		} 
    		if (!erreur.equals("")) { 
    			stringToSend.receiveMsg(erreur); 
 
    			return false; 
 
    		} 
 
    		// DERNIERE VERIF 
    		stringToSend.receiveMsg("[Info] Verification proprietes ..."); 
 
    		boolean erreurBoolean = false; 
    		if (!depart) { 
    			stringToSend.receiveMsg("[Erreur] Le plateau doit comporter une case depart"); 
    			erreurBoolean = true; 
    		} 
    		if (prison && !enPrison) { 
    			stringToSend.receiveMsg("[Erreur] Une prison doit avoir au moins 1 case 'aller en prison'"); 
    			erreurBoolean = true; 
    		} 
    		if (!prison && enPrison) { 
 
    			stringToSend.receiveMsg("[Erreur] Une case 'aller en prison' doit avoir une prison"); 
 
    			stringToSend.receiveMsg("[Erreur] Une case aller en prison doit avoir une prison"); 
 
    			erreurBoolean = true; 
    		} 
    		int nbGare = listeGare.size(); 
    		for (JSONObject caseInfo : listeGare) { // verif loyer gare 
    			if (caseInfo.getJSONArray("loyer").length() < nbGare) { 
    				stringToSend.receiveMsg( 
    						"[Erreur] Gare " + caseInfo.getString("nom") + " doit avoir au moins " + nbGare + " loyer."); 
    				erreurBoolean = true; 
    			} 
    		} 
    		int nbService = listeService.size(); 
    		for (JSONObject caseInfo : listeService) { // verif loyer gare 
    			if (caseInfo.getJSONArray("multiplicateur").length() < nbService) { 
    				stringToSend.receiveMsg("[Erreur] Service " + caseInfo.getString("nom") + " doit avoir au moins " 
    						+ nbService + " multiplicateur."); 
    				erreurBoolean = true; 
    			} 
    		} 
    		int groupe = -1; 
    		for (JSONObject caseInfo : listeTerrain) { 
    			if (caseInfo.getInt("groupe") > groupe + 1) { 
    				stringToSend.receiveMsg("[Erreur] Le groupe de " + caseInfo.getString("nom") + " est invalide. (" 
    						+ (groupe + 1) + " au max est attendu)"); 
    				erreurBoolean = true; 
    			} else { 
    				if (caseInfo.getInt("groupe") >= groupe) { 
    					groupe = caseInfo.getInt("groupe"); 
    				} 
    			} 
    		} 
 
    		if (!erreurBoolean) { 
    			stringToSend.receiveMsg("[Info] Configuration par defaut charge avec succes !"); 
    			stringToSend.receiveMsg("[Info] Plateau de " + listeCase.size() + " case(s) cree avec succes !"); 
    			configLoad = true; 
    		} else { 
    			return false; 
    		}    		 
        } catch (Exception e) {
        	stringToSend.receiveMsg("[Erreur] Fichier introuvable");
			return false;
		}
        return true; 
 
	} 
	
	public ArrayList<Case> getListeCase(){
		return listeCase;
	}
	
	public int getNbCase() {
		return nbCase;
	}
	public boolean isHypoteque() {
		return hypothequeEnable;
	}
	
	public void loadAction() { //Voir un autre moyen de le faire
		new Action1();
		new Action2();
		new Action3();
		new Action4();
		new Action5();
		new Action6();
		new Action7();
	}
	
	public boolean loadDefaultConfig() throws IOException, JSONException {
		configLoad = false;
		playerConfig = null;
		loadAction();
		stringToSend.receiveMsg("[Info] Lecture configuration ...");
		listeCaseJSON.clear();
		listeCase.clear();
		// CONVERTION DU FICHIER DE CONFIG EN JSON
		BufferedReader in = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("defaultConfig.json")));
		String inputLine;
		String response = "";

		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONObject configJSON = new JSONObject(response);
		if (Objects.isNull(configJSON)) {
			stringToSend.receiveMsg("[Info] Erreur lors du chargement de la configuration");

			return false;

		}

		// LECTURE ET TRAITEMENT DU FICHIER
		JSONObject gameSetting = configJSON.getJSONObject("GameSetting");
		JSONObject caseListInformation = configJSON.getJSONObject("Case");
		try {
			JSONObject playerListInformation = configJSON.getJSONObject("Player");
			playerConfig =playerListInformation;
		}catch (Exception e) {
			// TODO: handle exception
		}

		try {
			nbCase = gameSetting.getInt("case");

		} catch (Exception e) {
			stringToSend.receiveMsg("[Erreur] Parametre case mal renseigne. Tentative prise de valeur par defaut (20)");
		}
		try {
			nbJoueur = gameSetting.getInt("player");
		} catch (Exception e) {
			stringToSend
					.receiveMsg("[Erreur] Parametre player mal renseigne. Tentative prise de valeur par defaut (2)");
		}
		try {
			lvlMaxProp = gameSetting.getInt("niveauTerrain");
		} catch (Exception e) {
			stringToSend.receiveMsg(
					"[Erreur] Parametre niveauTerrain depart mal renseigne. Tentative prise de valeur par defaut (6)");
		}
		try {
			hypothequeEnable = gameSetting.getBoolean("hypotheque");
		} catch (Exception e) {
			stringToSend.receiveMsg(
					"[Erreur] Parametre hypotheque depart mal renseigne. Tentative prise de valeur par defaut (false)");
		}
		try {
			defaultMoney = gameSetting.getInt("defaultMoney");
		} catch (Exception e) {
			stringToSend.receiveMsg(
					"[Erreur] Parametre defaultMoney mal renseigne. Tentative prise de valeur par defaut (1500)");
		}
		try {
			caseListInformation.get(Integer.toString(nbCase - 1));
		} catch (Exception e) {
			stringToSend.receiveMsg("[Erreur] Nombre de cases insuffisant.");

			return false;

		}
		ArrayList<JSONObject> listeGare = new ArrayList<JSONObject>();
		ArrayList<JSONObject> listeService = new ArrayList<JSONObject>();
		ArrayList<JSONObject> listeTerrain = new ArrayList<JSONObject>();
		// CREATION DES CASES
		String erreur = "";
		boolean depart = false;
		boolean prison = false;
		boolean enPrison = false;
		stringToSend.receiveMsg("[Info] Verification des cases ...");
		for (int i = 0; i < nbCase; i++) {

			JSONObject caseInfo = null;
			try {
				caseInfo = caseListInformation.getJSONObject(Integer.toString(i));
			} catch (Exception e) {
				stringToSend.receiveMsg("[Erreur] Case " + i + " introuvable.");

				return false;

			}
			try {
				String type = caseInfo.getString("type");
				if (type.equals("depart")) {
					if (!depart) {
						if (i == 0) {
							try {
								caseInfo.getString("nom");
								caseInfo.getInt("gain");
								listeCaseJSON.add(caseInfo);
								caseInfo.remove("type");
								listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
								depart = true;
							} catch (Exception e) {
								erreur += "\n[Erreur] Propriete case depart non defini (nom/gain)";
							}
						} else {
							erreur += "\n[Erreur] La case depart doit avoir comme indice 0";
						}
					} else {
						erreur += "\n[Erreur] Le plateau doit comporter qu'une seul case depart";
					}

				} else if (type.equals("caseCarte")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type + " non defini (nom)";
					}

				} else if (type.equals("gare")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("prix");
						caseInfo.getJSONArray("loyer");
						caseInfo.getInt("hypoteque");
						listeGare.add(caseInfo);
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type
								+ " non defini (nom/prix/loyer/hypoteque)";
					}

				} else if (type.equals("terrain")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("groupe");
						caseInfo.getInt("prix");
						caseInfo.getInt("prixConstruction");
						caseInfo.getJSONArray("loyer");
						caseInfo.getInt("hypoteque");
						if (caseInfo.getJSONArray("loyer").length() < lvlMaxProp) {
							erreur += "\n[Erreur] " + lvlMaxProp + " prix sont exiges pour le loyer sur la case " + i;
						}
						listeTerrain.add(caseInfo);
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type
								+ " non defini (nom/groupe/prix/prixConstruction/loyer/hypoteque)";
					}

				} else if (type.equals("sansAction")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type + " non defini (nom)";
					}

				} else if (type.equals("prison")) {
					try {
						if (!prison) {
							caseInfo.getString("nom");
							caseInfo.getInt("amountToEscape");
							caseInfo.getInt("diceValue");
							listeCaseJSON.add(caseInfo);
							caseInfo.remove("type");
							listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
							prison = true;
						} else {
							erreur += "[Erreur] Le plateau doit comporter qu'une seul case prison";
						}
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type + " non defini (nom/amountToEscape/diceValue)";
					}

				} else if (type.equals("service")) {
					try {
						caseInfo.getString("nom");
						caseInfo.getInt("prix");
						caseInfo.getJSONArray("multiplicateur");
						caseInfo.getInt("hypoteque");
						listeCaseJSON.add(caseInfo);
						listeService.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type + " non defini (nom)";
					}
				} else if (type.equals("enPrison")) {
					try {
						caseInfo.getString("nom");
						listeCaseJSON.add(caseInfo);
						caseInfo.remove("type");
						listeCase.add(CaseFactory.getInstance().createCase(type, caseInfo, i));
						enPrison = true;
					} catch (Exception e) {
						erreur += "\n[Erreur] Propriete case n°" + i + " de type " + type + " non defini (nom)";
					}

				} else {
					erreur += "\n[Erreur] Type de la case n°" + i + " non valide";
				}

			} catch (Exception e) {
				stringToSend.receiveMsg("[Erreur] Case " + i + ": type non renseigne.");

				return false;

			}

		}
		if (!erreur.equals("")) {
			stringToSend.receiveMsg(erreur);

			return false;

		}

		// DERNIERE VERIF
		stringToSend.receiveMsg("[Info] Verification proprietes ...");

		boolean erreurBoolean = false;
		if (!depart) {
			stringToSend.receiveMsg("[Erreur] Le plateau doit comporter une case depart");
			erreurBoolean = true;
		}
		if (prison && !enPrison) {
			stringToSend.receiveMsg("[Erreur] Une prison doit avoir au moins 1 case 'aller en prison'");
			erreurBoolean = true;
		}
		if (!prison && enPrison) {

			stringToSend.receiveMsg("[Erreur] Une case 'aller en prison' doit avoir une prison");

			stringToSend.receiveMsg("[Erreur] Une case aller en prison doit avoir une prison");

			erreurBoolean = true;
		}
		int nbGare = listeGare.size();
		for (JSONObject caseInfo : listeGare) { // verif loyer gare
			if (caseInfo.getJSONArray("loyer").length() < nbGare) {
				stringToSend.receiveMsg(
						"[Erreur] Gare " + caseInfo.getString("nom") + " doit avoir au moins " + nbGare + " loyer.");
				erreurBoolean = true;
			}
		}
		int nbService = listeService.size();
		for (JSONObject caseInfo : listeService) { // verif loyer gare
			if (caseInfo.getJSONArray("multiplicateur").length() < nbService) {
				stringToSend.receiveMsg("[Erreur] Service " + caseInfo.getString("nom") + " doit avoir au moins "
						+ nbService + " multiplicateur.");
				erreurBoolean = true;
			}
		}
		int groupe = -1;
		for (JSONObject caseInfo : listeTerrain) {
			if (caseInfo.getInt("groupe") > groupe + 1) {
				stringToSend.receiveMsg("[Erreur] Le groupe de " + caseInfo.getString("nom") + " est invalide. ("
						+ (groupe + 1) + " au max est attendu)");
				erreurBoolean = true;
			} else {
				if (caseInfo.getInt("groupe") >= groupe) {
					groupe = caseInfo.getInt("groupe");
				}
			}
		}

		if (!erreurBoolean) {
			stringToSend.receiveMsg("[Info] Configuration par defaut charge avec succes !");
			stringToSend.receiveMsg("[Info] Plateau de " + listeCase.size() + " case(s) cree avec succes !");
			configLoad = true;
		} else {
			return false;
		}

		return true;

	}

	public int getDefaultMoney() {
		return defaultMoney;
	}

	public int getNbJoueur() {
		return nbJoueur;
	}

	public boolean isConfigLoad() {
		return configLoad;
	}

	public ArrayList<Integer> getLoyer(int i) {
		
		ArrayList<Integer> listeLoyer = CaseFactory.getInstance().JSONArrayToIntArrayList(listeCaseJSON.get(i).getJSONArray("loyer"));
		return listeLoyer;
	}

	public ArrayList<Integer> getMultiplicateur(int i) {
		ArrayList<Integer> multiplicateur = CaseFactory.getInstance().JSONArrayToIntArrayList(listeCaseJSON.get(i).getJSONArray("multiplicateur"));
		return multiplicateur;
	}

	public int getPrixConstruction(int i) {
		int prix = listeCaseJSON.get(i).getInt("prixConstruction");
		return prix;
	}

}
