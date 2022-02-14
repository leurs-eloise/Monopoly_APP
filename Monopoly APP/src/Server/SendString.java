package Server;
import java.util.ArrayList;
import java.util.Objects;

import Content.Configuration;

public class SendString {
	ArrayList<ReceiveEvent> ecouteur = new ArrayList<ReceiveEvent>();
	public SendString() {
	}
	
	private static SendString sendString;
	public static SendString getInstance() {
		if(Objects.isNull(sendString)) {
			sendString = new SendString();
		}
		return sendString;
	}
	
	public void receiveMsg(String msg) {
		for(ReceiveEvent e: ecouteur) {
			e.transferMsg(msg);
		}
	}
	public void addEcouteur(ReceiveEvent e) {
		ecouteur.add(e);
	}
	
}
