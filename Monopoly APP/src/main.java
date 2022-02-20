import java.io.IOException;

import org.json.JSONException;

import Content.Configuration;
import Content.Partie;
import Content.Case.Terrain;
import Server.Server;

public class main extends Thread  {
	
	public void run()
	{
			new Server();
	}
	
	public static void main(String args[]) throws JSONException, IOException {
		main test = new main();
		test.start();
		
		Configuration.getInstance().loadDefaultConfig();
		Partie.getInstance().init();
		System.out.println(Configuration.getInstance().getLoyer(12).get(1));
		//new Server();
		/*
		main test = new main();
		test.start();
		*/

		
	}
}
