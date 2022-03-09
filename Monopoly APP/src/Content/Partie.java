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
import Server.SendString;

public class Partie {
	private ArrayList<Joueur> listeJoueur = new ArrayList<Joueur>();
	private Joueur joueurActuel;
	private static Partie partie;
	private int etat = 0; // 0 partie non lancer - 1 d�but tour - 2-3 achat/vente/echange du joueur - 3
	// fin de tour
	private SendString stringToSend = SendString.getInstance();
	private int joueurPosInt = 0;
	private ArrayList<carteAction> listeCarteAction = new ArrayList<carteAction>();

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
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		if (!Configuration.getInstance().isConfigLoad()) {
			stringToSend.receiveMsg("[Erreur] Aucune configuration n'a �t� charg�e !");
			return false;
		}

		for (int i = 0; i < Configuration.getInstance().getNbJoueur(); i++) {
			listeJoueur.add(new Joueur(i, "Joueur " + (i + 1), Configuration.getInstance().getDefaultMoney()));
		}
		if(!Objects.isNull(Configuration.getInstance().getPlayerConfig())) {
			stringToSend.receiveMsg("[Info] Chargement configuration joueur");
			for(int i = 0; i < listeJoueur.size(); i++) {
				try {
					JSONObject configJoueur = Configuration.getInstance().getPlayerConfig().getJSONObject(Integer.toString(i+1));
					try {
						listeJoueur.get(i).setTourPrison(configJoueur.getInt("tourEnPrison"));
						stringToSend.receiveMsg("[Info] Joueur " + (i+1) + ": tour en prison d�fini");
					}catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setArgent(configJoueur.getInt("argent"));
						stringToSend.receiveMsg("[Info] Joueur " + (i+1) + ": argent d�fini");
					}catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setPosition(configJoueur.getInt("position"));
						stringToSend.receiveMsg("[Info] Joueur " + (i+1) + ": position d�fini");
					}catch (Exception e) {
					}
					try {
						listeJoueur.get(i).setPrisonCard(configJoueur.getInt("cartePrison"));
						stringToSend.receiveMsg("[Info] Joueur " + (i+1) + ": carte en prison d�fini");
					}catch (Exception e) {
					}
					try {
						JSONArray prop = configJoueur.getJSONArray("propriete");
						ArrayList<Integer> propList = CaseFactory.getInstance().JSONArrayToIntArrayList(prop);
						for(Integer idProp : propList) {
							try {
								((Propriete)Configuration.getInstance().getListeCase().get(idProp)).setJoueur(listeJoueur.get(i));
								stringToSend.receiveMsg("[Info] Joueur " + (i+1) + ": propri�taire de la case " + Configuration.getInstance().getListeCase().get(idProp).getNom());
							}catch (Exception e) {
								stringToSend.receiveMsg("[Erreur] La case " + propList.get(idProp) + " n'est pas une propri�t�");
							}
						}
					}catch (Exception e) {
					}
				}catch (Exception e) {
				}
			}
		}
		
		Random rand = new Random();
		joueurPosInt = rand.nextInt(listeJoueur.size());
		joueurActuel = listeJoueur.get(joueurPosInt);

		etat = 1;
		return true;
	}

	public boolean lancerDes() {
		try {
			if (etat != 1) {
				stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
				return false;
			}
			if (joueurActuel.getTourPrison() != 0) { // Lancer de d�s si joueur en prison

				stringToSend.receiveMsg(
						"[Info] " + joueurActuel.getPseudo() + " a choisit de lancer les d�s pour sortir de prison");
				joueurActuel.lancerDes();
				if (joueurActuel.getValDes() == 6) {
					stringToSend.receiveMsg(
							"[Info] " + joueurActuel.getPseudo() + " est sortit de prison en faisant un 6 !");
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " peut maintenant jouer");
					joueurActuel.setTourPrison(0);
					etat = 1;
					return true;
				} else {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " � fait un "
							+ joueurActuel.getValDes() + ". Il reste en prison.");
					etat = 3;
					return true;
				}
			} else { // Lancer de d�s classique
				joueurActuel.lancerDes();
				if (((joueurActuel.getPosition() + joueurActuel.getValDes()) > Configuration.getInstance().getNbCase())
						&& ((joueurActuel.getPosition() + joueurActuel.getValDes())
								% Configuration.getInstance().getNbCase()) != 0) {
					stringToSend.receiveMsg(
							"[Info] " + joueurActuel.getPseudo() + " est pass� par la case d�part et a re�u "
									+ ((Depart) Configuration.getInstance().getListeCase().get(0)).getPactole() + "$");
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
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
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
		stringToSend
		.receiveMsg("[Info] " + joueurActuel.getPseudo() + " ce retrouve sur la case " + currentCase.getNom());
		if (currentCase instanceof Prison) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est en visite simple sur la prison");
			etat = 3;
			return true;
		} else if (currentCase instanceof CaseCarte) {
			Random rdm = new Random();
			carteAction carte = listeCarteAction.get(rdm.nextInt(listeCarteAction.size()));
			etat = 3;
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " doit: " + carte.getDescription());
			carte.doAction();

			return true;
		} else if (currentCase instanceof Depart) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " per�oit un salaire de "
					+ ((Depart) Configuration.getInstance().getListeCase().get(0)).getPactole() + "$");
			((Depart) currentCase).pactole(joueurActuel);
			etat = 3;
			return true;
		} else if (currentCase instanceof Propriete) {
			Joueur owner = ((Propriete) currentCase).getJoueur();
			if (owner == null || owner == joueurActuel) {
				stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur " + currentCase.getNom());
				etat = 2;
				return true;
			} else {
				stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur " + currentCase.getNom()
				+ " qui est poss�d� par " + owner.getPseudo());
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
			return true;
		} else if (currentCase instanceof EnPrison) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " part en prison !");
			for (Case cas : Configuration.getInstance().getListeCase()) {
				if (cas instanceof Prison) {
					joueurActuel.setPosition(((Prison) cas).getId());
					currentCase = cas;
				}
			}

			stringToSend.receiveMsg("[Info] Pour sortir de prison, vous devez: \n- faire un "
					+ ((Prison) currentCase).getDes() + " \n-Payer " + ((Prison) currentCase).getEscape()
					+ "$ \n-Utiliser une carte 'sortir de prison'");
			joueurActuel.setTourPrison(1);
			etat = 3;
		} else {
			stringToSend.receiveMsg("[Erreur critique] Case non identifi�");
			stringToSend.receiveMsg("[Erreur critique] Fin de partie");
			etat = 0;
			return false;
		}
		return false;
	}

	public boolean skip() {
		if (etat != 2) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		etat = 3;
		return finTour();
	}

	public boolean payToEscape() {
		if ((etat != 1) || joueurActuel.getTourPrison() == 0) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		if (joueurActuel
				.getArgent() > ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition()))
				.getEscape()) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " � payer "
					+ ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition())).getEscape()
					+ "$ pour sortir de prison. Il peut jouer normalement");
			joueurActuel.setArgent(joueurActuel.getArgent()
					- ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition()))
					.getEscape());
			etat = 1;
			joueurActuel.setTourPrison(0);
		} else {
			stringToSend.receiveMsg("[Erreur] " + joueurActuel.getPseudo() + " ne dispose pas des sous n�cessaire");
			etat = 1;
		}
		return true;
	}

	public boolean useCard() {
		if ((etat != 1) || joueurActuel.getTourPrison() == 0) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		if (joueurActuel.getPrisonCard() > 0) {
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo()
			+ " � utiliser une carte 'sortir de prison'. Il peut jouer normalement");
			joueurActuel.setPrisonCard(joueurActuel.getPrisonCard() - 1);
			joueurActuel.setTourPrison(0);
			etat = 1;
		} else {
			stringToSend
			.receiveMsg("[Erreur] " + joueurActuel.getPseudo() + " ne dispose pas de carte 'sortir de prison'");
			etat = 1;
		}
		return true;
	}

	public boolean hypotheque() {
		if (etat != 2 && etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		} else {
			if(Configuration.getInstance().isHypoteque()) {
				stringToSend.receiveMsg("[Erreur] Les hypoth�ques sont d�sactiv�es !");
				return false;
			} else {
				return true;
			}
		}

	}

	public boolean echanger() {
		if (etat != 2 && etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}

		return true;
	}

	public boolean acheterBuilding(int level, Terrain ter){
		if (etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		getCurrentPlayer().acheterBuilding(level, ter);
		etat = 3;
		return true;
	}

	public boolean finTour() {
		if (etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		if (joueurActuel.getArgent() < 0) {
			for (Propriete prop : joueurActuel.getProprietes()) {
				prop.setJoueur(null);
			}
			joueurActuel.getProprietes().clear();
			listeJoueur.remove(joueurActuel);
			stringToSend.receiveMsg(
					"[Info] Le joueur " + joueurActuel.getPseudo() + " n'a plus d'argent. Il est donc �limin�");
		}
		if (listeJoueur.size() == 1) {
			stringToSend
			.receiveMsg("[Info] Fin de la partie. " + listeJoueur.get(0).getPseudo() + " gagne la partie !");
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
		if (joueurActuel.getTourPrison() > 0 && joueurActuel.getTourPrison() < 4) {
			stringToSend.receiveMsg("[info] " + joueurActuel.getPseudo() + " d�bute son tour en prison");
		} else {
			stringToSend.receiveMsg("[info] A " + joueurActuel.getPseudo() + " de jouer !");
		}

		if (joueurActuel.getTourPrison() >= 4) {
			joueurActuel.setArgent(joueurActuel.getArgent()
					- ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition()))
					.getEscape());
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est rest� trop de temps en prison. "
					+ joueurActuel.getPseudo() + " � pay� "
					+ ((Prison) Configuration.getInstance().getListeCase().get(joueurActuel.getPosition())).getEscape()
					+ "$");
			joueurActuel.setTourPrison(0);
			etat = 1;
			stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " peut maintenant lancer le d�s pour jouer");
			return true;
		}
		etat = 1;
		return true;
	}

	public String plateau() {
		if(!Configuration.getInstance().isConfigLoad()) {
			return "[Erreur] Aucune configuration n'est charg�";
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
				plateau += tempo +"\n";
			}

			if (i == 1) {
				if (reste != 0) {
					for (int j = 0; j < 4 - reste; j++) {
						String tempo = "";
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "--|";
						}
						tempo += getType(listeCase.get(longueur + j));
						plateau += tempo +"\n";


					}
					longueur -= 1;
					for (int j = 0; j < longueur - (4 - reste) - 1; j++) {
						String tempo = "";
						tempo = getType(listeCase.get(listeCase.size() - j - 1));
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "|--";
						}
						tempo += "|" + getType(listeCase.get(longueur + j + 1 + (4 - reste)));
						plateau += tempo +"\n";
					}

				} else {
					for (int j = 0; j < longueur - reste - 1; j++) {
						String tempo = "";
						tempo = getType(listeCase.get(listeCase.size() - j - 1));
						for (int k = 0; k < longueur - 1; k++) {
							tempo += "|--";


						}
						tempo += "|" + getType(listeCase.get(longueur + j + 1 + reste));
						plateau += tempo +"\n";
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
				plateau += tempo +"\n";
			}
		}

		return plateau;
	}

	public String getType(Case cases) {
		return cases.getClass().getSimpleName().subSequence(0, 2).toString();
	}

}
