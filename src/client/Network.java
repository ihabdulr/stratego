package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import client.Global.GameState;
import game.Board;
import game.SaveLoad;
import game.player.NetworkPlayer;
import server.Packets;

/**
 * Setups the client to attempt an connected to a server
 *
 * @author Aaron Roberts
 */

public class Network implements Runnable {

    public int PORT;
    public boolean DEBUG = true;
    public String SERVER;
    public ArrayList<String> commands = new ArrayList<String>();
    private DataInputStream dIn = null;
    private DataOutputStream dOut = null;
    private Socket clientConnection = null;
    private String clientAddress;
    private boolean connected = false;
    private String answer = "";
    private boolean started = false;

    public Network(int port, String server) {
        PORT = port;
        SERVER = server;
    }

    public void debug(String s) {
        if (DEBUG) {
            String timeStamp = new SimpleDateFormat("hh:mm:ss").format(new Date());
            System.out.println("\t[" + timeStamp + "]: " + s);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean connect() {
        if(connected)
            return true;
        dIn = null;
        dOut = null;
        clientConnection = null;
        connected = false;
        try {
            System.out.println("Attempting to connected to " + SERVER + " on port " + PORT);
            clientConnection = new Socket(SERVER, PORT);
            clientAddress = clientConnection.getLocalAddress().toString();
            System.out.println("Successfully connected client ["+clientAddress+"] to server. Sending message now");

            OutputStream out = clientConnection.getOutputStream();
            dOut = new DataOutputStream(out);

            InputStream in = clientConnection.getInputStream();
            dIn = new DataInputStream(in);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Failed to connect to " + SERVER + ":" + PORT);
            return false;
        }
        connected = true;
        return clientConnection != null;
    }

    public String getClientAddress() {
    	return clientAddress;
    }
    
    public void disconnect() {
        connected = false;
    }

    @Override
    public void run() {
        while (true) {
            if(connected) {
                try {
                    InputStream in = clientConnection.getInputStream();
                    dIn = new DataInputStream(in);
                	handleIncomingPacket(dIn.readUTF());
                   
                    
               
                     //debug(dIn.readUTF());


                } catch (SocketException e) {
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
        }
    }

    public void sendPacketToServer(String s) {
    	
    	if (( clientConnection.isClosed() || !(clientConnection.isConnected())) )
    		return;
    	if (clientConnection == null) 
    		return;
        DataOutputStream dOut = null;
        try {
            dOut = new DataOutputStream(clientConnection.getOutputStream());
            
            if ( (dOut != null)) {
        		//debug("Sending packet: "+s+" to server at "+clientConnection.getRemoteSocketAddress()); //Runs
        		
        		dOut.writeUTF(s);  
        		dOut.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void handleIncomingPacket(String s) { 
    	debug("INCOMING Packet from server: "+s);
    	if (s.startsWith(Packets.P_GIVING_PIECES)) {
    		//Server is sending us enemy pieces
    		
    		s = s.substring(Packets.P_GIVING_PIECES.length() + 1, s.length()); //+ 1 for the seperator
    		//SaveLoad.convertPieces(s);
    		System.out.println("Pieces");
    		//System.out.println(s);
    		NetworkPlayer np = (NetworkPlayer) Board.getEnemyPlayer();
    		np.setPieces(SaveLoad.convertPiecesWithFlip(s));
    		
    		Board.addPieces(np.getSanitizedPieces());
    		return;
    	} 
    	switch (s) {
    	
	    	case Packets.P_INSETUP: 
	    		Global.setBoardState(Global.BoardState.SETUP);
	    		Global.setGameState(GameState.GAME);
	    		Global.setGameType(true);
	    		System.out.println("Set gamestate");
	    		sendPacketToServer(Packets.P_STATUS_INSETUP);
	    		break;
	    	case Packets.P_INGAME:
	    		Global.setBoardState(Global.BoardState.MY_TURN); // Last one to call this will go first
	    		//Global.setGameState(Global.GameState.GAME);
	    		System.out.println("Set gamestate to in game");
	    		sendPacketToServer(Packets.P_STATUS_INGAME);

	    		break;
	    	

	    		
	    	default: 
	    		System.out.println("Unknown request from server: "+s);

	    		break;
    	}
    	
    }
    public String getAnswer() {
    	return answer;
    }
    
    
    
    
}
