package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import client.Global;
import game.Piece;
import game.SaveLoad;
import session.GameSession;
import session.Player;

/**
 * Handles an individual client connection
 * @author Aaron Roberts
 *
 */
public class SocketHandler implements Runnable {

    public boolean DEBUG = true;
    public Socket socket;
    public int id = 0;
    public Server hostServer;
    public String forcedPacket = "";
    public String clientsAddress = "";
    public Player clientsPlayer = null;
    public SocketHandler(Socket uniqueClient, int clientId, Server s, Player p) {
    	
	        this.socket = uniqueClient;
	        this.id = clientId;
	        this.hostServer = s;
	        clientsPlayer = p;
    }

    public void debug(String s) {
        if (DEBUG) {
            String timeStamp = new SimpleDateFormat("hh:mm:ss").format(new Date());
            System.out.println("\t[CLIENTID:" + id + "/" + timeStamp + "]: " + s);
        }
    }

    public Socket getSocket() {
    	return socket;
    }
    @Override
    public void run() {
   
        InputStream in = null;
        DataInputStream dIn = null;
        try {
            in = socket.getInputStream();
            dIn = new DataInputStream(in);
            debug("New client connected");
            sendPacketToClient("Welcome to the server!");
            clientsAddress = socket.getRemoteSocketAddress().toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
        	if (( socket.isClosed() || !(socket.isConnected())) ) {
        		hostServer.decrementClientCount(clientsAddress);
        		return;
        	}
        	if (socket == null) 
        		return;
            try {
                if (dIn != null) {
                	
                    String packet = dIn.readUTF();
                    String response = handlePacket(packet);
                    //sendPacketToClient(response);
                   // debug("Packet received: " + packet + " | Packet sent: " + response);

                    if (response.equals("Closing connection...")) {
                    	hostServer.decrementClientCount(getAddress());
                        socket.close();
                        
                        return;
                    }
                }
            } catch (SocketException e) {
	           	 
	             hostServer.decrementClientCount(getAddress());
	             return;
            }catch (IOException e) {
                e.printStackTrace();
            } 
            
        }
    }

    public void sendPacketToClient(String s) {
    	if (( socket.isClosed() || !(socket.isConnected())) )
    		return;
    	if (socket == null) 
    		return;
        DataOutputStream dOut = null;
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            
            if ( (dOut != null)) {
        		debug("Sending packet: "+s+" to client at "+socket.getRemoteSocketAddress()); //Runs
        		
        		dOut.writeUTF(s);  
        		dOut.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public Player getPlayer() {
    	return clientsPlayer;
    }

    public String getAddress() {
    	if (socket == null) {
    		return "Invalid address";
    	}
    	return socket.getRemoteSocketAddress().toString();
    }
    /**
     * @param s Incoming message from client
     * @return Returns the appropriate message, or an unknown request
     */
    public String handlePacket(String s) {
        System.out.println(s);
        String n = "";
      
    	if (s.startsWith(Packets.P_SEND_PIECES)) {
    		n = "Set player pieces";
    		s = s.substring(Packets.P_SEND_PIECES.length() + 1, s.length());
    		//debug("Server set pieces");
    		//debug(s);
    		clientsPlayer.setPlayerPieces(s);
    		clientsPlayer.printPieces();
    		clientsPlayer.setStatus(Player.Status.READY);
    		
    		return n;
    	}
    	
    	if (s.startsWith(Packets.P_REQUEST_PIECE)) {
    		s = s.substring(Packets.P_REQUEST_PIECE.length(), s.length());
    		java.util.List<String> integerList = Arrays.asList(s.split(Packets.P_SEPERATOR));
    		if (integerList.size() > 1)  {
    			int x = Integer.parseInt(integerList.get(0));
    			int y = Integer.parseInt(integerList.get(1));
    			Optional<Piece> p = hostServer.multiplayerQueue.getPlayerByAddress(socket.getLocalSocketAddress().toString()).getPiece(x, y);
    			if (p.isPresent()) 
    				n = SaveLoad.getPieceAsString(p.get());
    			n = "Couldn't find piece";
    		}
    		return n;
    	}
        switch (s) {

            case Packets.P_PING:
                n = "PONG";
                break;
            case Packets.P_REQUEST_ID:
                n = Integer.toString(id);
                break;
            case Packets.P_REQUEST_ADD:
                n = socket.getLocalSocketAddress().toString();
                break;
            case Packets.P_DESTROY:
                n = "Closing connection...";
                break;
            case Packets.P_QUEUE_PLAYER: 
            	n = "Adding client to matchmaking...";
            	//hostServer.multiplayerQueue.addPlayer(socket.getRemoteSocketAddress().toString());
            	hostServer.multiplayerQueue.addPlayer(clientsPlayer.getAddress());

            	debug("New player added to multiplayer: Queue contains "+hostServer.multiplayerQueue.getPlayers().size());
            	break;
            case Packets.P_REMOVE_FROM_QUEUE:
            	n = "Removing client from matchmaking...";
            	//Player p = hostServer.multiplayerQueue.getPlayerByAddress(clientsAddress);
            	hostServer.multiplayerQueue.removePlayer(clientsPlayer);
            	break;
         
            case Packets.P_STATUS_INSETUP:
        		clientsPlayer.setStatus(Player.Status.SETUP);
            	break;
            case Packets.P_STATUS_INGAME:
        		clientsPlayer.setStatus(Player.Status.IN_GAME);
            	break;	
            	
            	
            default:
                n = "Unknown request on call (" + s + ")";

        }
        return n;
    }
    
    
}
