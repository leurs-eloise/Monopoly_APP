package Content;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import Content.Case.Gare;
import Content.Case.Propriete;
import Content.Case.Service;
import Content.Case.Terrain;
import Server.ClientParty;
import Server.ClientServer;
import Server.SendString;

public class Joueur {
	private static final AtomicInteger ID_FACTORY = new AtomicInteger();
	private int id;
	private String pseudo;
	private int argent;
	private int position = 0;
	private int tourPrison = 0;
	private int valDes;
	private Des des = new Des();
	private ArrayList<Propriete> proprietes = new ArrayList<Propriete>();
	private int score;
	private SendString stringToSend = SendString.getInstance();
	private int prisonCard = 0;

	// constructeur pour la config
	public Joueur(int id, String pseudo, int argent, int position, int tourPrison, ArrayList<Propriete> proprietes) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
		this.position = position;
		this.tourPrison = tourPrison;
		this.proprietes = proprietes;
		this.setScore();
	}

	// constructeur simplifie pour la creation d'un joueur
	public Joueur(int id, String pseudo, int argent) {
		super();
		this.id = ID_FACTORY.getAndIncrement();
		this.pseudo = pseudo;
		this.argent = argent;
	}

	public void lancerDes() {
		this.valDes = des.getValeur();
		stringToSend.receiveMsg("[Info] " + pseudo + " a fait un " + valDes);
		ClientServer.sendMessage("tablet rolldice " + valDes);
	}

	public void acheter(Propriete prop) {
		if ((prop.getJoueur() == null) && (this.argent >= prop.getPrix())) {
			this.argent -= prop.getPrix();
			ClientParty.sendMessage("tablet setMoney " + getId() + " " + argent);
			prop.setJoueur(this);
			proprietes.add(prop);
			ClientParty.sendMessage("tablet addPropriete " + prop.getId() + " " + this.getId() + " " + prop.getNom());
			
			String message = pseudo + " a achetai " + prop.getNom() + " pour " + prop.getPrix() + " polypoints";
			ClientParty.sendMessage("pepper sayHappy " + message);
			stringToSend.receiveMsg("[Info] " + message);
			}
	}

	public int getValDes() {
		return valDes;
	}

	public void payer(Terrain prop) {
		Joueur proprietaire = prop.getJoueur();
		if ((proprietaire != null) && (proprietaire != this)) {
			int loyer = prop.getListeLoyer().get(prop.getNbBuilding());
			proprietaire.setArgent(proprietaire.getArgent() + loyer);
			this.setArgent(this.getArgent() - loyer);
			ClientParty.sendMessage("tablet setMoney " + proprietaire.getId() + " " + proprietaire.getArgent());
			ClientParty.sendMessage("tablet setMoney " + getId() + " " + argent);
			
			String message = pseudo + " a payai " + loyer + " polypoints a " + proprietaire.getPseudo();
			ClientParty.sendMessage("pepper sayClignote " + message);
			stringToSend.receiveMsg("[Info] " + message);
		}
	}

	public void payer(Gare prop) {
		Joueur proprietaire = prop.getJoueur();
		if ((proprietaire != null) && (proprietaire != this)) {
			proprietaire.setArgent(proprietaire.getArgent() + prop.getLoyer());
			this.setArgent(this.getArgent() - prop.getLoyer());
			ClientParty.sendMessage("tablet setMoney " + proprietaire.getId() + " " + proprietaire.getArgent());
			ClientParty.sendMessage("tablet setMoney " + getId() + " " + argent);
			
			String message = pseudo + " a payai " + prop.getLoyer() + " polypoints a " + proprietaire.getPseudo();
			ClientParty.sendMessage("pepper sayClignote " + message);
			stringToSend.receiveMsg("[Info] " + message);
		}
	}

	public void payer(Service prop) {
		Joueur proprietaire = prop.getJoueur();
		if ((proprietaire != null) && (proprietaire != this)) {
			this.lancerDes();
			proprietaire.setArgent(proprietaire.getArgent() + prop.getNiveau() * this.getValDes());
			System.out.println(prop.getNiveau() + " - " + this.getValDes());
			this.setArgent(this.getArgent() - prop.getNiveau() * this.getValDes());
			ClientParty.sendMessage("tablet setMoney " + proprietaire.getId() + " " + proprietaire.getArgent());
			ClientParty.sendMessage("tablet setMoney " + getId() + " " + argent);
			
			String message = pseudo + " a payai " + prop.getNiveau() * valDes + " polypoints a " + proprietaire.getPseudo();
			ClientParty.sendMessage("pepper sayClignote " + message);
			stringToSend.receiveMsg("[Info] " + message);
			}
	}

	public void demandeConfirmation(Propriete prop1, Propriete prop2, int sous) {
		String message = prop1.getJoueur().getPseudo() + " veut aichanger "
				+ prop1.getNom() + " avec " + prop2.getNom() + " et " + sous
				+ " polypoints. Acceptez-vous cet aichange ?";
		ClientParty.sendMessage("msg PepperReceive" + message);
		ClientParty.sendMessage("pepper msg " + message);
	}

	// le joueur veut aichanger la prop1 contre la prop2 d'un autre joueur avec x
	// sous
	public void echanger(Propriete prop1, Propriete prop2, int sous) {
		Joueur proprietaire = prop1.getJoueur();
		Joueur destinataire = prop2.getJoueur();
		if ((proprietaire != null) && (proprietaire == this) && (destinataire != null) && (destinataire != this)) {
			prop1.setJoueur(destinataire);
			prop2.setJoueur(proprietaire);
			destinataire.setArgent(destinataire.getArgent() + sous);
			proprietaire.setArgent(proprietaire.getArgent() - sous);
			
			String message = "aichange effectuai";
			ClientParty.sendMessage("pepper say " + message);
			stringToSend.receiveMsg("[Erreur] " + message);
		} else {
			String message = "Vous ne pouvez pas faire cet aichange";
			ClientParty.sendMessage("pepper say " + message);
			stringToSend.receiveMsg("[Erreur] " + message);
		}
	}

	public void acheterBuilding(int nombre, Terrain ter) {
		if (ter.getJoueur() == this) {
			if (((ter.getNbBuilding() + nombre) < 5)
					&& (this.argent >= Configuration.getInstance().getPrixConstruction(ter.getId()))) {
				this.argent -= Configuration.getInstance().getPrixConstruction(ter.getId());
				ClientParty.sendMessage("tablet setMoney " + getId() + " " + argent);
				ter.setNbBuilding(nombre);
				
				String message = "Vous venez d'acheter " + nombre + " maisons sur " + ter.getNom();
				ClientParty.sendMessage("pepper sayHappy " + message);
				stringToSend.receiveMsg("[Info] " + message);
			}
		}
	}

	public void hypotheque(Propriete prop) {
		prop.setHypotheque(false);
		this.setArgent(this.getArgent() + prop.getPrixHypotheque());
	}

	// Getter and setter
	public void setScore() {
		this.score = this.argent;
		for (Propriete prop : this.getProprietes()) {
			this.score += prop.getPrix();
			if (prop instanceof Terrain) {
				this.score += ((Terrain) prop).getNbBuilding()
						* Configuration.getInstance().getPrixConstruction(prop.getId());
			}
		}
	}

	public int getScore() {
		return this.score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getArgent() {
		return argent;
	}

	public void setArgent(int argent) {
		this.argent = argent;
	}

	public int getNbProp() {
		return proprietes.size();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getTourPrison() {
		return tourPrison;
	}

	public void setTourPrison(int tourPrison) {
		this.tourPrison = tourPrison;
	}

	public Des getDes() {
		return des;
	}

	public void setDes(Des des) {
		this.des = des;
	}

	public ArrayList<Propriete> getProprietes() {
		return proprietes;
	}

	public void setProprietes(ArrayList<Propriete> proprietes) {
		this.proprietes = proprietes;
	}

	public int getPrisonCard() {
		return prisonCard;
	}

	public void setPrisonCard(int prisonCard) {
		this.prisonCard = prisonCard;
	}

}
