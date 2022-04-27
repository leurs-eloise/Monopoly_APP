package Server;

import java.net.*;
import java.util.Objects;

import Content.Configuration;

import java.io.*;

public class ClientParty {
	public static void sendMessage(String message) {
		new ClientParty(message);
	}

	public ClientParty(String msg) {
		try {
			String clientMessage = msg, serverMessage = "";
			Socket socket = new Socket("127.0.0.1", 39039);
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

			outStream.writeUTF(clientMessage);
			outStream.flush();
			serverMessage = inStream.readUTF();
			SendString.getInstance().receiveMsg(serverMessage);

			Thread.sleep(200);
			outStream.close();
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
