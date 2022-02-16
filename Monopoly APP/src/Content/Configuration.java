package Content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import Content.Case.Case;
import Content.Case.CaseFactory;
import Server.SendString;

public class Configuration {
	private int nbCase = 20;
	private int nbJoueur = 2;
	private int lvlMaxProp = 6;
	
	private ArrayList<JSONObject> listeCaseJSON = new ArrayList<JSONObject>();
	private ArrayList<Case> listeCase = new ArrayList<Case>();
	private SendString stringToSend = SendString.getInstance();
	
			
	private static Configuration configuration;
	public Configuration() {}
	
	public static Configuration getInstance() {
		if(Objects.isNull(configuration)) {
			configuration = new Configuration();
		}
		return configuration;
	}

	/*
	public boolean loadConfig(String path)  {
        try
        {
            File file = new File(path);
            if(!Desktop.isDesktopSupported())
            {
                System.out.println("not supported");
                return false;
            }
            Desktop desktop = Desktop.getDesktop();
            if(file.exists()) {
                desktop.open(file);
		        System.out.println(file);
		        return true;
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;

    
		
	}
	*/

	
	
	public boolean loadDefaultConfig() throws IOException, JSONException {

		stringToSend.receiveMsg("[Info] Lecture configuration ...");
		listeCaseJSON.clear();
		listeCase.clear();
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
			stringToSend.receiveMsg("[Info] Erreur lors du chargement de la configuration");

			return false;

		}
		
		// LECTURE ET TRAITEMENT DU FICHIER
		JSONObject gameSetting = configJSON.getJSONObject("GameSetting");
		JSONObject caseListInformation = configJSON.getJSONObject("Case");
		try {
			nbCase = gameSetting.getInt("case");
			nbJoueur = gameSetting.getInt("player");
			lvlMaxProp = gameSetting.getInt("niveauTerrain");
		}catch (Exception e) {
			stringToSend.receiveMsg("[Erreur] Paramètre case/joueur/level mal renseigné. Tentative prise de valeur par défaut (20 cases et 2 joueurs)");
		}
		try{
			caseListInformation.get(Integer.toString(nbCase-1));
		}catch (Exception e) {
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
		stringToSend.receiveMsg("[Info] Vérification des cases ...");
		for(int i=0; i<nbCase; i++) {


			JSONObject caseInfo = null;
			try {
				caseInfo= caseListInformation.getJSONObject(Integer.toString(i));
			}catch (Exception e) {
				stringToSend.receiveMsg("[Erreur] Case " + i + " introuvable.");

				return false;


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
						enPrison = true;
					}catch (Exception e) {
						erreur+="\n[Erreur] Propriété case n°"+ i +" de type "+ type + " non défini (nom)";
					}
					
				} else {
					erreur += "\n[Erreur] Type de la case n°" + i + " non valide";
				}

			}catch (Exception e) {
				stringToSend.receiveMsg("[Erreur] Case " + i + ": type non renseigné.");

				return false;

			}

			
		}
		if(!erreur.equals("")) {
			stringToSend.receiveMsg(erreur);

			return false;

		}
		
		// DERNIERE VERIF
		stringToSend.receiveMsg("[Info] Vérification propriétés ...");

		
		boolean erreurBoolean = false;
		if(!depart) {
			stringToSend.receiveMsg("[Erreur] Le plateau doit comporter une case départ");
			erreurBoolean = true;
		}
		if(prison && !enPrison) {
			stringToSend.receiveMsg("[Erreur] Une prison doit avoir au moins 1 case 'aller en prison'");
			erreurBoolean = true;
		}
		if(!prison && enPrison) {

			stringToSend.receiveMsg("[Erreur] Une case 'aller en prison' doit avoir une prison");

			stringToSend.receiveMsg("[Erreur] Une case aller en prison doit avoir une prison");

			erreurBoolean = true;
		}
		int nbGare = listeGare.size();
		for(JSONObject caseInfo : listeGare) { //verif loyer gare
			if(caseInfo.getJSONArray("loyer").length() < nbGare) {
				stringToSend.receiveMsg("[Erreur] Gare " + caseInfo.getString("nom") + " doit avoir au moins " + nbGare + " loyer.");
				erreurBoolean = true;
			}
		}
		int nbService = listeService.size();
		for(JSONObject caseInfo : listeService) { //verif loyer gare
			if(caseInfo.getJSONArray("multiplicateur").length() < nbService) {
				stringToSend.receiveMsg("[Erreur] Service " + caseInfo.getString("nom") + " doit avoir au moins " + nbService + " multiplicateur.");
				erreurBoolean = true;
			}
		}
		int groupe = -1;
		for(JSONObject caseInfo : listeTerrain) {
			if(caseInfo.getInt("groupe") > groupe +1) {
				stringToSend.receiveMsg("[Erreur] Le groupe de "+ caseInfo.getString("nom") + " est invalide. (" + (groupe+1) + " au max est attendu)");
				erreurBoolean = true;
			} else {
				if(caseInfo.getInt("groupe")>= groupe) {
					groupe = caseInfo.getInt("groupe");
				}
			}
		}
		
		
		if(!erreurBoolean) {
			stringToSend.receiveMsg("[Info] Configuration par défaut chargé avec succès !");
			stringToSend.receiveMsg("[Info] Plateau de " + listeCase.size() + " case(s) créé avec succès !");
		} else {
			return false;
		}
		
		return true;

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
