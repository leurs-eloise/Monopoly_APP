package Content.Case.Action;

import Content.Joueur;
import Content.Partie;
import Server.ClientParty;

public class Action1 implements carteAction {

	private String description = "Reculer de 3 cases";
	@SuppressWarnings("unused")
	private Joueur joueur = Partie.getInstance().getCurrentPlayer();

	// Constructeur
	public Action1() {
		Partie.getInstance().addAction(this);
	}

	public String getDescription() {
		return description;
	}

	@Override
	public void doAction() {
		Partie.getInstance().getCurrentPlayer().setPosition(Partie.getInstance().getCurrentPlayer().getPosition() - 3); // Reculer
		ClientParty.sendMessage("tablet case nom:Case_Action action:" + description.replace(" ", "_"));																											// de
																														// 3
																														// cases
		Partie.getInstance().actualiserPosition();

	}

}
