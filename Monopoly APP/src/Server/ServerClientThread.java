package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Content.Configuration;
import Content.Partie;
import Content.Case.Case;
import Content.Case.Terrain;

class ServerClientThread extends Thread {
	Socket serverClient;
	int clientNo;
	ServerSocket mainServer;
	String clientName;
	Server mainSocket;
	ListeRobot listeRobot;
	SendString sendMessage;

	ServerClientThread(Socket inSocket, ServerSocket server, ListeRobot robot, SendString stringToSend) {
		serverClient = inSocket;
		mainServer = server;
		clientName = "";
		listeRobot = robot;
		sendMessage = stringToSend;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Socket getSocket() {
		return this.serverClient;
	}

	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
			DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
			String clientMessage = "", serverMessage = "";
			while (!clientMessage.equals("disconnect")) {
				clientMessage = inStream.readUTF();
				String[] clientMessageSplit = clientMessage.split(" ");
				if (!clientName.equals("ClientServer")) {
					if (!clientMessage.equals("setname ClientServer")) {
						sendMessage.receiveMsg("[Info] " + clientName + ": " + clientMessage);
					}
				} else {
					sendMessage.receiveMsg(">> " + clientMessage);
				}
				if (clientMessageSplit[0].equals("setname")) {
					if (clientMessageSplit.length == 1 || clientMessageSplit.length > 2) {
						serverMessage = "[Erreur] Usage: setname (name)";
					}
					if (clientMessageSplit.length == 2) {
						for (ServerClientThread client : listeRobot.getRobot()) {
							if (client.getClientName().equals(clientMessageSplit[1])) {
								client.getSocket().close();
								serverMessage = "[Error] Name already taken. Leaving duplicate ...";
							}
						}
						this.setClientName(clientMessageSplit[1]);
						if (!clientName.equals("ClientServer")) {
							serverMessage = serverMessage + "[Info] Your name is now " + this.getClientName();
						}

					}

				} else if (clientMessageSplit[0].equals("msg")) {
					if (clientMessageSplit.length < 3) {
						serverMessage = "[Erreur] Usage: msg (robot) (message)";
					}
					if (clientMessageSplit.length >= 3) {
						if (clientMessageSplit[1].equals(this.clientName)
								|| clientMessageSplit[1].equals("PepperSend")) {
							serverMessage = "[Error] You can't send message to this client !";
						} else {
							for (ServerClientThread client : listeRobot.getRobot()) {
								if (client.getClientName().equals(clientMessageSplit[1])) {
									DataOutputStream outStreamClient = new DataOutputStream(
											client.getSocket().getOutputStream());
									String message = "";
									for (int i = 2; i <= clientMessageSplit.length - 1; i++) {
										if (message.equals("")) {
											message = message + clientMessageSplit[i];
										} else {
											message = message + " " + clientMessageSplit[i];
										}
									}
									outStreamClient.writeUTF(message);
									outStreamClient.flush();
									serverMessage = "[Info] Send '" + message + "' to " + clientMessageSplit[1];
									break;
								} else {
									serverMessage = "[Error] Client not found";
								}
							}
						}

					}

				} else if (clientMessageSplit[0].equals("help")) {
					serverMessage = "[Info] Liste des commandes: " + "buy\r\n" + "exchange\r\n" + "fintour\r\n"
							+ "help\r\n" + "home\r\n" + "hypotheque\r\n" + "loadconfig\r\n" + "mc\r\n" + "msg\r\n"
							+ "msgall\r\n" + "paytoescape\r\n" + "plateau\r\n" + "robot\r\n" + "rolldice\r\n"
							+ "setname\r\n" + "skip\r\n" + "start\r\n" + "usecard\r\n" + "web\r\n";

				} else if (clientMessageSplit[0].equals("stop")) {
					for (ServerClientThread client : listeRobot.getRobot()) {
						if (!(client == this)) {
							DataOutputStream outStreamClient = new DataOutputStream(
									client.getSocket().getOutputStream());
							serverMessage = "[Info] Stopping server ...";
							outStreamClient.writeUTF(serverMessage);
							outStreamClient.flush();
							outStreamClient.close();
							client.getSocket().close();
						}
					}

					serverMessage = "[Info] Stopping server ...";
					outStream.writeUTF(serverMessage);
					outStream.flush();
					outStream.close();
					serverClient.close();
					mainServer.close();

				} else if (clientMessageSplit[0].equals("robot")) {
					serverMessage = "[Info] List of connected robot:  ";
					for (ServerClientThread client : listeRobot.getRobot()) {
						if (client == this) {
							serverMessage = serverMessage + "(" + getClientName() + ") ";
						} else {
							serverMessage = serverMessage + client.getClientName() + " ";
						}
					}

				} else if (clientMessageSplit[0].equals("msgall")) {
					String message = "";
					for (int i = 1; i <= clientMessageSplit.length - 1; i++) {
						if (message.equals("")) {
							message = message + clientMessageSplit[i];
						} else {
							message = message + " " + clientMessageSplit[i];
						}

					}
					serverMessage = "Send to everyone: " + message;
					for (ServerClientThread client : listeRobot.getRobot()) {
						DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
						if (!(client == this) && !(client.getClientName().equals("PepperSend"))) {
							outStreamClient.writeUTF(message);
						}
					}

				} else if (clientMessageSplit[0].equals("mc")) {
					String message = "";
					for (int i = 1; i <= clientMessageSplit.length - 1; i++) {
						if (message.equals("")) {
							message = message + clientMessageSplit[i];
						} else {
							message = message + " " + clientMessageSplit[i];
						}

					}
					serverMessage = "Send to Minecraft: " + message;
					for (ServerClientThread client : listeRobot.getRobot()) {
						if (client.getClientName().equals("ReceiveMinecraft")) {
							DataOutputStream outStreamClient = new DataOutputStream(
									client.getSocket().getOutputStream());
							outStreamClient.writeUTF(message);
						}
					}
				} else if (clientMessageSplit[0].equals("web")) {
					String message = "";
					for (int i = 1; i <= clientMessageSplit.length - 1; i++) {
						if (message.equals("")) {
							message = message + clientMessageSplit[i];
						} else {
							message = message + " " + clientMessageSplit[i];
						}
					}
					serverMessage = "Send to WebSite: " + message;
					for (ServerClientThread client : listeRobot.getRobot()) {
						if (client.getClientName().equals("ServerWebReceive")) {
							DataOutputStream outStreamClient = new DataOutputStream(
									client.getSocket().getOutputStream());
							outStreamClient.writeUTF(message);
						}
					}

				} else if (clientMessageSplit[0].equals("loadconfig")) {
					if(clientMessageSplit.length > 1) {
						Configuration.getInstance().loadConfig(clientMessageSplit[1]);
					} else {
						Configuration.getInstance().loadDefaultConfig();
					}
				}

				else if (clientMessageSplit[0].equals("rolldice")) {
					if (Partie.getInstance().lancerDes()) {
						serverMessage = "[Info] Dés lancé";
					} else {
						serverMessage = "[Erreur] Erreur lors du lancement des dés";
					}
				}

				else if (clientMessageSplit[0].equals("hypotheque")) {
					Partie.getInstance().hypotheque();
				}


				else if (clientMessageSplit[0].equals("buy")) {
					Partie.getInstance().acheter();
				}

				else if (clientMessageSplit[0].equals("exchange")) {
					//je recois prop1 puis prop2 puis sous
					Partie.getInstance().echanger(); // mettre la fonction qui fait l'echange
				}

				else if (clientMessageSplit[0].equals("home")) {
					if (clientMessageSplit.length == 2) {
						try {
							int level = Integer.parseInt(clientMessageSplit[1]);
							if (level < 0 || level > 6) {
								serverMessage = "[Erreur] Le niveau doit être compris entre 0 et 5";
							} else {
								for (Case item : Configuration.getInstance().getListeCase()) {
									if (item.getNom().equals(clientMessageSplit[1])
											&& item.getType().equals("Terrain")) {
										Terrain ter = (Terrain) item;
										Partie.getInstance().acheterBuilding(Integer.parseInt(clientMessageSplit[2]), ter);
									}
								}
							}
						} catch (Exception e) {
							serverMessage = "[Erreur] Le niveau doit être un nombre";
						}
					}

					else {
						serverMessage = "[Erreur] Usage: home (level)";
					}
				}

				else if (clientMessageSplit[0].equals("bilan")) {
					serverMessage = Partie.getInstance().getCurrentPlayer().getPseudo() + " possede "
							+ Partie.getInstance().getCurrentPlayer().getNbProp() + " proprietes et "
							+ Partie.getInstance().getCurrentPlayer().getArgent() + " sous.";

				}

				else if (clientMessageSplit[0].equals("fin")) {
					Partie.getInstance().finTour();
				}

				else if (clientMessageSplit[0].equals("usecard")) {
					Partie.getInstance().useCard();
				} else if (clientMessageSplit[0].equals("paytoescape")) {
					Partie.getInstance().payToEscape();
				} else if (clientMessageSplit[0].equals("skip")) {
					Partie.getInstance().skip();
				} else if (clientMessageSplit[0].equals("start")) {
					if (Partie.getInstance().init()) {
						serverMessage = "[Info] Partie démarée" + "\nA "
								+ Partie.getInstance().getCurrentPlayer().getPseudo() + " de jouer !";
					} else {
						serverMessage = "[Erreur] Erreur lors du lancement de la partie";
					}
				} else if (clientMessageSplit[0].equals("fintour")) {
					Partie.getInstance().finTour();
				} else if (clientMessageSplit[0].equals("plateau")) {
					serverMessage = Partie.getInstance().plateau();
				}

				else {
					serverMessage = "[Info] Type 'help' to get commands list";
				}
				// System.out.println("[Info] " + clientName + ": "+clientMessage);

				outStream.writeUTF(serverMessage);
				outStream.flush();

			}
			inStream.close();
			outStream.close();
			listeRobot.delRobot(this);
			serverClient.close();
		} catch (Exception ex) {
			if (!clientName.equals("ClientServer")) {
				sendMessage.receiveMsg("[Error] " + String.valueOf(ex));
			}
		} finally {
			if (!clientName.equals("ClientServer")) {
				sendMessage.receiveMsg("[Quit] " + clientName + " has left the server ...");
			}
			listeRobot.delRobot(this);
		}
	}
}
