import java.io.IOException;

import org.json.JSONException;

import Content.Configuration;
import Content.Partie;
import Content.Case.Terrain;
import Server.Server;

public class main extends Thread  {
	
	public void run()
	{
			//new OBEXPutServer();
			new Server();
	}
	
	public static void main(String args[]) throws JSONException, IOException {
		main test = new main();
		test.start();
		
		//Configuration.getInstance().loadConfig("test.json");
		//Partie.getInstance().init();
		//new Server();
		/*
		main test = new main();
		test.start();
		*/

		
	}
}
