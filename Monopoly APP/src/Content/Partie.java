package Content;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import Content.Case.Case;
import Content.Case.CaseCarte;
import Content.Case.CaseFactory;
import Content.Case.Depart;
import Content.Case.EnPrison;
import Content.Case.Gare;
import Content.Case.Prison;
import Content.Case.Propriete;
import Content.Case.SansAction;
import Content.Case.Service;
import Content.Case.Terrain;
import Content.Case.Action.carteAction;
import Server.ClientParty;
import Server.SendString;

public class Partie {
	private ArrayList<Joueur> listeJoueur = new ArrayList<Joueur>();
	private Joueur joueurActuel;
	private static Partie partie;
	private int etat = 0; // 0 partie non lancer - 1 debut tour - 2-3 achat/vente/echange du joueur - 3
	// fin de tour
	private SendString stringToSend = SendString.getInstance();
	private int joueurPosInt = 0;
	private ArrayList<carteAction> listeCarteAction = new ArrayList<carteAction>();
	private ArrayList<Object> dmdEch = null;

	public Partie() {
	}

	public static Partie getInstance() {
		if (Objects.isNull(partie)) {
			partie = new Partie();
		}
		return partie;
	}

	public void addAction(carteAction action) {
		listeCarteAction.add(action);
	}

	public Joueur getCurrentPlayer() {
		return joueurActuel;
	}

	// init
	public boolean init() {

		if (!listeJoueur.isEmpty()) {
			listeJoueur.clear();
		}
		if (etat != 0) {
			String message = "Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (!Configuration.getInstance().isConfigLoad()) {
			stringToSend.receiveMsg("[Erreur] Aucune configuration n'a ete chargee !");
			return false;
		}

		for (int i = 0; i < Configuration.getInstance().getNbJoueur(); i++) {
			ClientParty.sendMessage("tablet initPlayer " + i + " " + Configuration.getInstance().getDefaultMoney());
			listeJoueur.add(new Joueur(i, "Joueur " + (i + 1), Configuration.getInstance().getDefaultMoney()));
		}
		if (!Objects.isNull(Configuration.getInstance().getPlayerConfig())) {
			stringToSend.receiveMsg("[Info] Chargement configuration joueur");
			for (int i = 0; i < listeJoueur.size(); i++) {
				try {
					JSONObject configJoueur = Configuration.getInstance().getPlayerConfig()
							.getJSONObject(Integer.toString(i + 1));
					try {
						listeJoueur.get(i).setTourPrison(configJoueur.getInt("tourEnPrison"));
						stringToSend.receiveMsg("[Info] Joueur " + (i + 1) + ": tour en prison defini");
					} catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setArgent(configJoueur.getInt("argent"));
						ClientParty.sendMessage("tablet setMoney " + i + " " + configJoueur.getInt("argent"));
						stringToSend.receiveMsg("[Info] Joueur " + (i + 1) + ": argent defini");
					} catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setPosition(configJoueur.getInt("position"));
						stringToSend.receiveMsg("[Info] Joueur " + (i + 1) + ": position definie");
					} catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setPrisonCard(configJoueur.getInt("cartePrison"));
						stringToSend.receiveMsg("[Info] Joueur " + (i + 1) + ": carte en prison definie");
						// tablette joueur nbCartePrison
					} catch (Exception e) {
					}
					try {
						JSONArray prop = configJoueur.getJSONArray("propriete");
						ArrayList<Integer> propList = CaseFactory.getInstance().JSONArrayToIntArrayList(prop);
						for (Integer idProp : propList) {
							try {
								((Propriete) Configuration.getInstance().getListeCase().get(idProp))
								.setJoueur(listeJoueur.get(i));
								stringToSend.receiveMsg("[Info] Joueur " + (i + 1) + ": proprietaire de la case "
										+ Configuration.getInstance().getListeCase().get(idProp).getNom());
								ClientParty.sendMessage("tablet addPropriete " + Configuration.getInstance().getListeCase().get(idProp).getNom() + " " + (i+1) + " " + Configuration.getInstance().getListeCase().get(idProp).getNom());
							} catch (Exception e) {
								stringToSend.receiveMsg(
										"[Erreur] La case " + propList.get(idProp) + " n'est pas une propriete");
							}
						}
					} catch (Exception e) {
					}
				} catch (Exception e) {
				}
			}
		}

		Random rand = new Random();
		joueurPosInt = rand.nextInt(listeJoueur.size());
		joueurActuel = listeJoueur.get(joueurPosInt);
		ClientParty.sendMessage("tablet afficherP " + joueurActuel.getId());
		etat = 1;
		return true;
	}

	public boolean bilan() {
		if(etat != 0) {
			String message = "bilan " + Partie.getInstance().getCurrentPlayer().getPseudo() + " possede "
					+ Partie.getInstance().getCurrentPlayer().getNbProp() + " proprietes et "
					+ Partie.getInstance().getCurrentPlayer().getArgent() + " polypoints.";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper " + message);
			return true;
		} else {
			String message = "Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
	}

	public boolean lancerDes() {
		try {
			if (etat != 1) {
				String message = "Vous ne pouvez pas faire cela maintenant !";
				stringToSend.receiveMsg("[Erreur] " + message);
				ClientParty.sendMessage("pepper say " + message);
				return false;
			}
			if (joueurActuel.getTourPrison() != 0) { // Lancer de des si joueur en prison

				String txt = joueurActuel.getPseudo() + " a choisit de lancer les des pour sortir de prison";
				stringToSend.receiveMsg("[Info] " + txt);
				ClientParty.sendMessage("pepper say " + txt);

				joueurActuel.lancerDes();
				if (joueurActuel.getValDes() == 6) {
					String message = joueurActuel.getPseudo() + " est sortie de prison en faisant un 6 ! Il peut maintenant jouer.";
					stringToSend.receiveMsg("[Info] " + message);
					ClientParty.sendMessage("pepper play " + message);
					joueurActuel.setTourPrison(0);
					etat = 1;
					return true;
				} else {
					String message = joueurActuel.getPseudo() + " a fait un " + joueurActuel.getValDes()
					+ ". Il reste en prison.";
					stringToSend.receiveMsg("[Info] " + message);
					ClientParty.sendMessage("pepper play " + message);
					etat = 3;
					return true;
				}
			} else { // Lancer de des classique
				joueurActuel.lancerDes();
				if (((joueurActuel.getPosition() + joueurActuel.getValDes()) > Configuration.getInstance().getNbCase())
						&& ((joueurActuel.getPosition() + joueurActuel.getValDes())
								% Configuration.getInstance().getNbCase()) != 0) {
					String message = joueurActuel.getPseudo() + " est passe par la case depart et a recu "
							+ ((Depart) Configuration.getInstance().getListeCase().get(0)).getPactole() + "polypoints";
					stringToSend.receiveMsg("[Info] " + message);
					ClientParty.sendMessage("pepper sayPactole " + message);

					((Depart) Configuration.getInstance().getListeCase().get(0)).pactole(joueurActuel);
				}
				joueurActuel.setPosition((joueurActuel.getPosition() + joueurActuel.getValDes())
						% Configuration.getInstance().getNbCase());
				return actualiserPosition();
			}
		} catch (Exception e) {
			stringToSend.receiveMsg(e + "");
			return false;
		}

	}

	public boolean acheter() {

		if (etat != 2) {
			String message = "Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		} else {
			Case currentCase = Configuration.getInstance().getListeCase().get(joueurActuel.getPosition());
			if (currentCase instanceof Propriete) {
				joueurActuel.acheter((Propriete) currentCase);
			}
		}

		etat = 3;
		return true;
	}

	public boolean actualiserPosition() {
		Case currentCase = Configuration.getInstance().getListeCase().get(joueurActuel.getPosition());
		String txt = joueurActuel.getPseudo() + " se retrouve sur la case " + currentCase.getNom();
		stringToSend.receiveMsg("[Info] " + txt);
		ClientParty.sendMessage("pepper say " + txt);

		if (currentCase instanceof Prison) {
			String message = joueurActuel.getPseudo() + " est en visite simple sur la prison";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper say " + message);

			etat = 3;
			ClientParty.sendMessage("tablet case nom:" + currentCase.getNom().replace(" ", "_"));

			return true;
		} else if (currentCase instanceof CaseCarte) {
			Random rdm = new Random();
			carteAction carte = listeCarteAction.get(rdm.nextInt(listeCarteAction.size()));
			etat = 3;
			String message = joueurActuel.getPseudo() + " doit: " + carte.getDescription();
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper say " + message);
			carte.doAction();
			// tablette afficher case carte
			return true;
		} else if (currentCase instanceof Depart) {
			String message = joueurActuel.getPseudo() + " percoit un salaire de "
					+ ((Depart) Configuration.getInstance().getListeCase().get(0)).getPactole() + "polypoints";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper sayHappy " + message);

			((Depart) currentCase).pactole(joueurActuel);
			ClientParty.sendMessage("tablet case nom:Case_Départ action:+" + ((Depart) Configuration.getInstance().getListeCase().get(0)).getPactole() + "_Polypoints");
			etat = 3;
			return true;
		} else if (currentCase instanceof Propriete) {
			Joueur owner = ((Propriete) currentCase).getJoueur();
			if (owner == null || owner == joueurActuel) {
				// tablette afficher propriete
				etat = 2;
				ClientParty.sendMessage("tablet case nom:" + currentCase.getNom().replace(" ", "_") + " prix:" + ((Propriete)currentCase).getPrix() + " loyer:" + getLoyer((Propriete)currentCase) + " hypothèque:" + ((Propriete)currentCase).getPrixHypotheque());
				return true;

			} else {
				// tablette afficher propriete avec owner et niveau de propriete actuelle
				String message = joueurActuel.getPseudo() + " est sur " + currentCase.getNom() + " qui est possede par "
						+ owner.getPseudo();
				stringToSend.receiveMsg("[Info] " + message);
				ClientParty.sendMessage("pepper say " + message);
				String loyer = getLoyer((Propriete)currentCase);
				int level = 0;
				if (currentCase instanceof Service) {
					level = ((Service)currentCase).getLevel();
				} else if (currentCase instanceof Gare) {
					level = ((Gare)currentCase).getLevel();
				} else if (currentCase instanceof Terrain) {
					level = ((Terrain)currentCase).getNbBuilding();
				}
				ClientParty.sendMessage("tablet case nom:" + currentCase.getNom().replace(" ", "_") + " Proprietaire:"+ owner.getPseudo().replace(" ", "_") +" level:"+ level +" prix:" + ((Propriete)currentCase).getPrix() + " loyer:" + loyer + " hypothèque:" + ((Propriete)currentCase).getPrixHypotheque());

				if (((Propriete) currentCase).isHypotheque() == false) {
					if (currentCase instanceof Service) {
						joueurActuel.payer(((Service) currentCase));
					} else if (currentCase instanceof Gare) {
						joueurActuel.payer(((Gare) currentCase));
					} else if (currentCase instanceof Terrain) {
						joueurActuel.payer(((Terrain) currentCase));
					}
				}


				etat = 3;
				return true;
			}

		} else if (currentCase instanceof SansAction) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est au repos");
			etat = 3;
			ClientParty.sendMessage("tablet case nom:" + currentCase.getNom().replace(" ", "_"));
			return true;
		} else if (currentCase instanceof EnPrison) {
			ClientParty.sendMessage("tablet case nom:" + currentCase.getNom().replace(" ", "_") + " action:Aller_en_prison" );
			String message = joueurActuel.getPseudo() + " part en prison !";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper say " + message);
			for (Case cas : Configuration.getInstance().getListeCase()) {
				if (cas instanceof Prison) {
					joueurActuel.setPosition(((Prison) cas).getId());
					currentCase = cas;
				}
			}

			message = "Pour sortir de prison, vous devez: \n- faire un " + ((Prison) currentCase).getDes()
					+ " \n-ou Payer " + ((Prison) currentCase).getEscape()
					+ "polypoints \n-ou Utiliser une carte 'sortir de prison'";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper prison " + message);
			joueurActuel.setTourPrison(1);
			etat = 3;
			return true;
		} else {
			stringToSend.receiveMsg("[Erreur critique] Case non identifie");
			stringToSend.receiveMsg("[Erreur critique] Fin de partie");
			etat = 0;
			return false;
		}
	}

	public boolean skip() {
		if (etat != 2) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		etat = 3;
		return finTour();
	}

	public boolean payToEscape() {
		if ((etat != 1) || joueurActuel.getTourPrison() == 0) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (joueurActuel
				.getArgent() > ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition()))
				.getEscape()) {
			String message = joueurActuel.getPseudo() + " a payer "
					+ ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition())).getEscape()
					+ "polypoints pour sortir de prison. Il peut jouer normalement";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper playHappy " + message);
			etat = 1;
			joueurActuel.setTourPrison(0);
		} else {
			String message = joueurActuel.getPseudo() + " ne dispose pas des polypoints necessaires";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper pbPrison " + message);
			etat = 1;
		}
		return true;
	}

	public String getLoyer(Propriete prop) {
		String loyer = "";
		if (prop instanceof Service) {
			for(int i : Configuration.getInstance().getMultiplicateur(prop.getId())) {
				loyer += Integer.toString(i) + "x_Valeur_des_dés,";
			}
			loyer.subSequence(0, loyer.length()-1);
		} else if (prop instanceof Gare) {
			for(int i : Configuration.getInstance().getLoyer(prop.getId())) {
				loyer += Integer.toString(i) + ",";
			}
			loyer.subSequence(0, loyer.length()-1);
		} else if (prop instanceof Terrain) {
			for(int i : ((Terrain) prop).getListeLoyer()) {
				loyer += Integer.toString(i) + ",";
			}
		}
		loyer.subSequence(0, loyer.length()-2);
		return loyer;
	}

	public boolean useCard() {
		if ((etat != 1) || joueurActuel.getTourPrison() == 0) {
			String message = "Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (joueurActuel.getPrisonCard() > 0) {
			String message = joueurActuel.getPseudo()
					+ " a utilisai une carte 'sortir de prison'. Il peut jouer normalement";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper playHappy " + message);

			joueurActuel.setPrisonCard(joueurActuel.getPrisonCard() - 1);
			joueurActuel.setTourPrison(0);
			etat = 1;
			// tablette afficher carte prison -1
		} else {
			String message = joueurActuel.getPseudo() + " ne dispose pas de carte 'sortir de prison'";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper pbPrison " + message);
			etat = 1;
		}
		return true;
	}

	public boolean hypotheque() {
		if (etat != 2 && etat != 3) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		} else {
			if (Configuration.getInstance().isHypoteque()) {
				stringToSend.receiveMsg("[Erreur] Les hypotheques sont desactivees !");
				return false;
			} else {
				return true;
			}
		}

	}

	public boolean demandeEchange(Propriete prop1, Propriete prop2, int sous) {
		if (etat != 2 && etat != 3) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (prop1.equals(prop2)) {
			String message = " Vous ne pouvez pas echanger une propriete avec elle meme";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
		} else {
			if (prop1.getJoueur().equals(prop2.getJoueur())) {
				String message = " Vous possedez deja cette propriete";
				stringToSend.receiveMsg("[Erreur] " + message);
				ClientParty.sendMessage("pepper say " + message);
			} else {
				dmdEch.add(prop1);
				dmdEch.add(prop2);
				dmdEch.add(sous);
				prop2.getJoueur().demandeConfirmation(prop1, prop2, sous);
			}
		}
		return true;
	}

	public boolean accepteEchange(boolean choix) {
		if (etat != 2 && etat != 3) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (!dmdEch.isEmpty()) {
			if (choix) {
				getCurrentPlayer().echanger((Propriete) (dmdEch.get(0)), (Propriete) (dmdEch.get(1)),
						(int) (dmdEch.get(2)));
				return true;
			}
		}
		return false;
	}

	public boolean acheterBuilding(int level, Terrain ter) {
		if (etat != 3) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (level < 0 || level > 6) {
			String message = " Le niveau doit etre compris entre 0 et 5";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
		} else {
			getCurrentPlayer().acheterBuilding(level, ter);
			etat = 3;
		}
		return true;
	}

	public boolean finTour() {
		if (etat != 3) {
			String message = " Vous ne pouvez pas faire cela maintenant !";
			stringToSend.receiveMsg("[Erreur] " + message);
			ClientParty.sendMessage("pepper say " + message);
			return false;
		}
		if (joueurActuel.getArgent() < 0) {
			for (Propriete prop : joueurActuel.getProprietes()) {
				prop.setJoueur(null);
			}
			joueurActuel.getProprietes().clear();
			listeJoueur.remove(joueurActuel);
			String message = "Le joueur " + joueurActuel.getPseudo() + " n'a plus d'argent. Il est donc elimine";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper sayTriste " + message);
		}
		if (listeJoueur.size() == 1) {
			String message = "Fin de la partie. " + listeJoueur.get(0).getPseudo() + " gagne la partie !";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper sayGagner " + message);
			etat = 0;
			return true;
		}
		if (joueurActuel.getTourPrison() != 0) {
			joueurActuel.setTourPrison(joueurActuel.getTourPrison() + 1);
		}
		joueurPosInt += 1;
		if (joueurPosInt >= listeJoueur.size()) {
			joueurPosInt = 0;
		}
		joueurActuel = listeJoueur.get(joueurPosInt);
		ClientParty.sendMessage("tablet afficherP " + joueurActuel.getId());
		if (joueurActuel.getTourPrison() > 0 && joueurActuel.getTourPrison() < 4) {
			String message = joueurActuel.getPseudo() + " debute son sejour en prison";
			ClientParty.sendMessage("tablet prison " + joueurActuel.getId() + " " + joueurActuel.getTourPrison());
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper say " + message);
			// tablette afficher menu prison

		} else {
			String message = joueurActuel.getPseudo() + " de jouer !";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper sayTour " + message);
			ClientParty.sendMessage("tablet prison " + joueurActuel.getId() + " " + joueurActuel.getTourPrison());
			// tablette afficher menu classique
		}

		if (joueurActuel.getTourPrison() >= 4) {
			joueurActuel.setArgent(joueurActuel.getArgent()
					- ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition()))
					.getEscape());
			String message = joueurActuel.getPseudo() + " est reste trop de temps en prison. " + "Il a paye "
					+ ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition())).getEscape()
					+ "polypoints";

			joueurActuel.setTourPrison(0);
			ClientParty.sendMessage("tablet prison " + joueurActuel.getId() + " " + joueurActuel.getTourPrison());
			etat = 1;
			message = message + joueurActuel.getPseudo() + " peut maintenant lancer le des pour jouer";
			stringToSend.receiveMsg("[Info] " + message);
			ClientParty.sendMessage("pepper play " + message);
			return true;
		}
		etat = 1;
		return true;
	}

	public String plateau() {
		if (!Configuration.getInstance().isConfigLoad()) {
			return "[Erreur] Aucune configuration n'est charge";
		}
		String plateau = "\n";
		ArrayList<Case> listeCase = Configuration.getInstance().getListeCase();
		int longueur = (int) Configuration.getInstance().getListeCase().size() / 4;
		int reste = Configuration.getInstance().getListeCase().size() % 4;

		for (int i = 0; i < 3; i++) {
			if (reste != 0) {
				longueur += 1;
			}

			if (i == 0) {
				String tempo = "";

				for (int j = 0; j < longueur + 1; j++) {
					if (j == 0) {
						tempo = tempo + "" + getType(listeCase.get(j));
					} else {
						tempo = tempo + "|" + getType(listeCase.get(j));
					}

				}
				plateau += tempo + "\n";
			}

			if (i == 1) {
				if (reste != 0) {
					for (int j = 0; j < 4 - reste; j++) {
						String tempo = "";
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "--|";
						}
						tempo += getType(listeCase.get(longueur + j));
						plateau += tempo + "\n";

					}
					longueur -= 1;
					for (int j = 0; j < longueur - (4 - reste) - 1; j++) {
						String tempo = "";
						tempo = getType(listeCase.get(listeCase.size() - j - 1));
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "|--";
						}
						tempo += "|" + getType(listeCase.get(longueur + j + 1 + (4 - reste)));
						plateau += tempo + "\n";
					}

				} else {
					for (int j = 0; j < longueur - reste - 1; j++) {
						String tempo = "";
						tempo = getType(listeCase.get(listeCase.size() - j - 1));
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "|--";

						}
						tempo += "|" + getType(listeCase.get(longueur + j + 1 + reste));
						plateau += tempo + "\n";
					}

				}
			}

			if (i == 2) {
				String tempo = "";
				if (reste != 0) {
					longueur -= 1;
					for (int j = 0; j < longueur + 1; j++) {
						if (j == 0) {
							tempo = getType(listeCase.get(listeCase.size() - longueur - j + (4 - reste)));
						} else {
							tempo += "|" + getType(listeCase.get(listeCase.size() - longueur - j + (4 - reste)));
						}
					}
				} else {
					for (int j = 0; j < longueur + 1; j++) {
						if (j == 0) {
							tempo = getType(listeCase.get(listeCase.size() - longueur - j));
						} else {
							tempo += "|" + getType(listeCase.get(listeCase.size() - longueur - j));
						}
					}

				}
				plateau += tempo + "\n";
			}
		}

		return plateau;
	}

	public String getType(Case cases) {
		return cases.getClass().getSimpleName().subSequence(0, 2).toString();
	}

}
