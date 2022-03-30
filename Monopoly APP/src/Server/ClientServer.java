package Server;

import java.net.*;
import java.util.Objects;

import Content.Configuration;

import java.io.*;

public class ClientServer {
	private static ClientServer clientServer;

	public static void sendMessage(String message) {
		clientServer = new ClientServer(message);
	}

	public ClientServer(String msg) {
		try {
			String clientMessage = msg, serverMessage = "";
			Socket socket = new Socket("127.0.0.1", 39039);
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
			outStream.writeUTF("setname ClientServer");
			outStream.flush();
			serverMessage = inStream.readUTF();

			outStream.writeUTF(clientMessage);
			outStream.flush();
			serverMessage = inStream.readUTF();
			SendString.getInstance().receiveMsg(serverMessage);

			outStream.close();
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
