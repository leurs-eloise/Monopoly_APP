package Content.Case.Action;

import Content.Joueur;
import Content.Partie;
import Server.ClientParty;

public class Action2 implements carteAction {

	private String description = "Aller jusqu'a la case d�part";
	@SuppressWarnings("unused")
	private Joueur joueur = Partie.getInstance().getCurrentPlayer();

	// Constructeur
	public Action2() {
		Partie.getInstance().addAction(this);
	}

	// Getter & Setter
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setPosition(0); // Avancer case depart
		ClientParty.sendMessage("tablet case nom:Case_Action action:" + description.replace(" ", "_"));
		Partie.getInstance().actualiserPosition();

	}

}
