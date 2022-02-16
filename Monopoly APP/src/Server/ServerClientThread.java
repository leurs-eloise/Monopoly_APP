package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Content.Configuration;

class ServerClientThread extends Thread {
  Socket serverClient;
  int clientNo;
  ServerSocket mainServer; 
  String clientName;
  Server mainSocket;
  ListeRobot listeRobot;
  SendString sendMessage;
  
  ServerClientThread(Socket inSocket, ServerSocket server, ListeRobot robot, SendString stringToSend){
    serverClient = inSocket;
    mainServer=server;
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

public void run(){
    try{
      DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
      DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
      String clientMessage="", serverMessage="";
      while(!clientMessage.equals("disconnect")){
        clientMessage=inStream.readUTF();
        String[] clientMessageSplit = clientMessage.split(" ");

        if(clientMessageSplit[0].equals("setname")) {
            if(clientMessageSplit.length == 1 || clientMessageSplit.length >2) {
            	serverMessage="[Erreur] Usage: setname (name)";
            }
            if(clientMessageSplit.length == 2) {
            	for(ServerClientThread client: listeRobot.getRobot()) {
            		if(client.getClientName().equals(clientMessageSplit[1])) {
            			client.getSocket().close();
            			serverMessage = "[Error] Name already taken. Leaving duplicate ...";
            		}
            	}
                this.setClientName(clientMessageSplit[1]);  
                serverMessage= serverMessage + "[Info] Your name is now " + this.getClientName();
            }
            
            
            
            
        } else if(clientMessageSplit[0].equals("msg")) {
            if(clientMessageSplit.length < 3) {
            	serverMessage="[Erreur] Usage: msg (robot) (message)";
            }
            if(clientMessageSplit.length >= 3) {
            	if(clientMessageSplit[1].equals(this.clientName) || clientMessageSplit[1].equals("PepperSend")) {
            		serverMessage="[Error] You can't send message to this client !";
            	} else {
	            	for(ServerClientThread client: listeRobot.getRobot()) {
	            		if(client.getClientName().equals(clientMessageSplit[1])) {
	            		    DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
	            		    String message = "";
	            		    for(int i = 2; i <= clientMessageSplit.length-1; i++) {
	            		    	if(message.equals("")) {
	                		    	message = message +  clientMessageSplit[i];
	            		    	} else {
	                		    	message = message + " "+  clientMessageSplit[i];
	            		    	}
	            		    }
	            		    outStreamClient.writeUTF(message);
	            		    outStreamClient.flush();
	                    	serverMessage="[Info] Send '" + message + "' to " + clientMessageSplit[1];
	                    	break;
	            		} else {
	            			serverMessage="[Error] Client not found";
	            		}
	            	}
	            }
            
            }
            
            
        } else if(clientMessageSplit[0].equals("help")) {
        	serverMessage="[Info] Liste des commandes: msg, setname, disconnect, stop, robot, mc, web, loadconfig";
        	
        	
        	
        }  else if(clientMessageSplit[0].equals("stop")) {
        	for(ServerClientThread client: listeRobot.getRobot()) {
        		if(!(client == this)) {
        		    DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
                	serverMessage="[Info] Stopping server ...";
                	outStreamClient.writeUTF(serverMessage);
                	outStreamClient.flush();
                	outStreamClient.close();
                    client.getSocket().close();
        		}
        	}
        	
        	serverMessage="[Info] Stopping server ...";
        	outStream.writeUTF(serverMessage);
        	outStream.flush();
        	outStream.close();
            serverClient.close();
        	mainServer.close();
        	
        	
        	
        	
        }  else if(clientMessageSplit[0].equals("robot")) {
        	serverMessage="[Info] List of connected robot:  ";
        	for(ServerClientThread client: listeRobot.getRobot()) {
        		if(client == this) {
        			serverMessage= serverMessage + "(" + getClientName() + ") ";
        		} else {
        			serverMessage= serverMessage + client.getClientName() + " ";
        		}
        	}
        	
        	
        	
        	
        	
        } else if(clientMessageSplit[0].equals("msgall")) {
		    String message = "";
		    for(int i = 1; i <= clientMessageSplit.length-1; i++) {
		    	if(message.equals("")) {
    		    	message = message +  clientMessageSplit[i];
		    	} else {
    		    	message = message + " "+  clientMessageSplit[i];
		    	}

		    }
		    serverMessage = "Send to everyone: " + message;
        	for(ServerClientThread client: listeRobot.getRobot()) {
    		    DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
        		if(!(client == this) && !(client.getClientName().equals("PepperSend"))) {
        			outStreamClient.writeUTF(message);
        		}
        	}
        	
        	
        } else if(clientMessageSplit[0].equals("mc")) {
        	String message = "";
		    for(int i = 1; i <= clientMessageSplit.length-1; i++) {
		    	if(message.equals("")) {
    		    	message = message +  clientMessageSplit[i];
		    	} else {
    		    	message = message + " "+  clientMessageSplit[i];
		    	}

		    }
		    serverMessage = "Send to Minecraft: " + message;
        	for(ServerClientThread client: listeRobot.getRobot()) {
        		if(client.getClientName().equals("ReceiveMinecraft")) {
        			DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
        			outStreamClient.writeUTF(message);
        		}
        	}
        } else if(clientMessageSplit[0].equals("web")) {
        	String message = "";
		    for(int i = 1; i <= clientMessageSplit.length-1; i++) {
		    	if(message.equals("")) {
    		    	message = message +  clientMessageSplit[i];
		    	} else {
    		    	message = message + " "+  clientMessageSplit[i];
		    	}
		    }
		    serverMessage = "Send to WebSite: " + message;
        	for(ServerClientThread client: listeRobot.getRobot()) {
        		if(client.getClientName().equals("ServerWebReceive")) {
        			DataOutputStream outStreamClient = new DataOutputStream(client.getSocket().getOutputStream());
        			outStreamClient.writeUTF(message);
        		}
        	}
        } else if(clientMessageSplit[0].equals("loadconfig")) {
        	Configuration.getInstance().loadDefaultConfig();
        } 
        
        
        
        
        
        
        else {
        	serverMessage= "[Info] Type 'help' to get commands list";
        }
        //System.out.println("[Info] " + clientName + ": "+clientMessage);
        if(!clientName.equals("ClientServer")) {
        	sendMessage.receiveMsg("[Info] " + clientName + ": "+clientMessage);
        } else {
        	sendMessage.receiveMsg(">> " + clientMessage);
        }
        outStream.writeUTF(serverMessage);
        outStream.flush();
        
      }
      inStream.close();
      outStream.close();
      listeRobot.delRobot(this);
      serverClient.close();
    }catch(Exception ex){
     if(!clientName.equals("ClientServer")) {
    	 sendMessage.receiveMsg("[Error] "+String.valueOf(ex));
     }  
    }finally{
      if(!clientName.equals("ClientServer")) {
    	  sendMessage.receiveMsg("[Quit] " + clientName + " has left the server ...");
      }
      listeRobot.delRobot(this);
    }
  }
}