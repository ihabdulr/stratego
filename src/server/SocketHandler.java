package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import client.Global;
import game.Piece;
import game.SaveLoad;
import session.Player;

/**
 * Handles an individual client connection
 * @author Aaron Roberts
 *
 */
public class SocketHandler extends Thread {

    public boolean DEBUG = true;
    public Socket socket;
    public int id = 0;
    public Server hostServer;
    public String forcedPacket = "";

    public SocketHandler(Socket uniqueClient, int clientId, Server s) {
    	
	        this.socket = uniqueClient;
	        this.id = clientId;
	        this.hostServer = s;
    	
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
        DataOutputStream dOut = null;
        try {
            in = socket.getInputStream();
            dIn = new DataInputStream(in);
            dOut = new DataOutputStream(socket.getOutputStream());
            debug("New client connected");
            dOut.writeUTF("Welcome to the server");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
        	if (( socket.isClosed() || !(socket.isConnected())) )
        		return;
        	if (socket == null) 
        		return;
            try {
                if ((dIn != null) && (dOut != null)) {
                	if (forcedPacket.equals("")) {
                		dOut.writeUTF("Listening...");
                	} else { 
                		dOut.writeUTF(forcedPacket);
                		forcedPacket = "";
                	}
                    String packet = dIn.readUTF();
                    String response = handlePacket(packet, false);
                    dOut.writeUTF(response);
                    debug("Packet received: " + packet + " | Packet sent: " + response);

                    if (response.equals("Closing connection...")) {
                        socket.close();
                        hostServer.decrementClientCount(id);
                        return;
                    }
                }
            } catch (SocketException e) {
	           	 
	             hostServer.decrementClientCount(id);
	             return;
            }catch (IOException e) {
                e.printStackTrace();
            } 
            
        }
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
    public String handlePacket(String s, boolean addToList) {
        System.out.println(s);
        String n = "";
        if (addToList)
        	forcedPacket = n;
    	if (s.startsWith(Packets.P_SEND_PIECES)) {
    		n = "Sending pieces to server...";
    		s = s.substring(Packets.P_SEND_PIECES.length(), s.length());
    		Player p = hostServer.multiplayerQueue.getPlayerByAddress(socket.getLocalSocketAddress().toString());
    		p.setPlayerPieces(s);
    		p.setReady(true);
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
            	hostServer.multiplayerQueue.addPlayer(socket.getLocalSocketAddress().toString());
            	debug("New player added to multiplayer: Queue contains "+hostServer.multiplayerQueue.getPlayers().size());
            	break;
            case Packets.P_REMOVE_FROM_QUEUE:
            	n = "Removing client from matchmaking...";
            	hostServer.multiplayerQueue.removePlayer(socket.getLocalSocketAddress().toString());
            	break;
            case Packets.P_INSETUP:
            	n = Packets.P_INSETUP;
            	break;
            case Packets.P_INGAME: 
            	n = Packets.P_INGAME;
            	break;
            default:
                n = "Unknown request on call (" + s + ")";

        }
        return n;
    }
}
