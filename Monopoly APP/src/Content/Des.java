package Content;

import java.util.Random;

public class Des {

	public Des() {}

	public int getValeur() {
		Random rand = new Random();
		int alea = rand.nextInt(6)+1;
		return alea;
	}
	
}
