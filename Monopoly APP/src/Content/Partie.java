package Content;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import Content.Case.Case;
import Content.Case.CaseCarte;
import Content.Case.Depart;
import Content.Case.EnPrison;
import Content.Case.Gare;
import Content.Case.Prison;
import Content.Case.Propriete;
import Content.Case.SansAction;
import Content.Case.Service;
import Content.Case.Terrain;
import Server.SendString;

public class Partie {
	private ArrayList<Joueur> listeJoueur = new ArrayList<Joueur>();
	private Joueur joueurActuel;
	private static Partie partie;
	private int etat = 0; // 0 partie non lancer - 1 début tour - 2-3 achat/ventre/echange du joueur - 3 fin de tour
	private SendString stringToSend = SendString.getInstance();
	private int joueurPosInt = 0;

	public Partie() {
	}

	public static Partie getInstance() {
		if (Objects.isNull(partie)) {
			partie = new Partie();
		}
		return partie;
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
			stringToSend.receiveMsg("[Erreur] Aucune configuration n'a été chargée !");
			return false;
		}

		for (int i = 0; i < Configuration.getInstance().getNbJoueur(); i++) {
			listeJoueur.add(new Joueur(i, "Joueur " + (i + 1), Configuration.getInstance().getDefaultMoney()));
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
			if (joueurActuel.getTourPrison() != 0) { // Lancer de dés si joueur en prison  A FAIRE
				return false;
			} else { // Lancer de dés classique
				joueurActuel.lancerDes();
				
				if(((joueurActuel.getPosition() + joueurActuel.getValDes())> Configuration.getInstance().getNbCase()) && ((joueurActuel.getPosition() + joueurActuel.getValDes()) % Configuration.getInstance().getNbCase()) != 0 ) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est passé par la case départ et a reçu " + ((Depart)Configuration.getInstance().getListeCase().get(0)).getPactole() + "$");
					((Depart)Configuration.getInstance().getListeCase().get(0)).pactole(joueurActuel);
				}
				joueurActuel.setPosition((joueurActuel.getPosition() + joueurActuel.getValDes()) % Configuration.getInstance().getNbCase());
				Case currentCase = Configuration.getInstance().getListeCase().get(joueurActuel.getPosition());
				stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " ce retrouve sur la case " + currentCase.getNom());

				if (currentCase instanceof Prison) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est en visite simple sur la prison");
					etat = 3;
				} else if (currentCase instanceof CaseCarte) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur CaseCarte");
				} else if (currentCase instanceof Depart) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " perçoit un salaire de " + ((Depart)Configuration.getInstance().getListeCase().get(0)).getPactole() + "$");
					((Depart) currentCase).pactole(joueurActuel);
				} else if (currentCase instanceof Terrain) {
					Joueur owner = ((Terrain)currentCase).getJoueur();
					if(owner == null) { //A check pk ca planter
						stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur " + currentCase.getNom());
						etat = 2;
					} else {
						stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur " + currentCase.getNom() + " qui est possédé par " + owner);
						joueurActuel.payer(((Terrain)currentCase));
						etat = 3;
						return true;
					}
					
				} else if (currentCase instanceof SansAction) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur SansAction");
					etat = 3;
				} else if (currentCase instanceof Service) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur Service");
					etat = 3;
				} else if (currentCase instanceof Gare) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur Gare");
					etat = 3;
				} else if (currentCase instanceof EnPrison) {
					stringToSend.receiveMsg("[Info] " + joueurActuel.getPseudo() + " est sur EnPrison");
					etat = 3;
				} else {
					stringToSend.receiveMsg("[Erreur critique] Case non identifié");
					stringToSend.receiveMsg("[Erreur critique] Fin de partie");
					etat = 0;
					return false;
				}
			}
			etat = 2;
			return true;
		}catch (Exception e) {
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
			if(currentCase instanceof Propriete) {
				joueurActuel.acheter((Propriete)currentCase);
			}
		}

		etat = 3;
		return true;
	}

	public boolean skip() {
		if (etat != 2) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		etat = 3;
		return finTour();
	}

	public boolean hypotheque() {
		if (etat != 2 || etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		etat = 2;
		return true;
	}

	public boolean echanger() {
		if (etat != 2 || etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}

		etat = 3;
		return true;
	}

	public boolean acheterBuilding(int level) {
		if (etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		etat = 3;
		return true;
	}

	public boolean finTour() {
		if (etat != 3) {
			stringToSend.receiveMsg("[Erreur] Vous ne pouvez pas faire cela maintenant !");
			return false;
		}
		joueurPosInt += 1;
		if(joueurPosInt >= listeJoueur.size()) {
			joueurPosInt = 0;
		}
		joueurActuel = listeJoueur.get(joueurPosInt);
		stringToSend.receiveMsg("[info] A " + joueurActuel.getPseudo() + " de jouer !");
		etat = 1;
		return true;
	}

}
