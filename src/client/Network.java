package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import client.Global.GameState;
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
    private boolean connected = false;
    private String answer = "";


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
            System.out.println("Successfully connected. Sending message now");

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

    public void disconnect() {
        connected = false;
    }

    @Override
    public void run() {
        while (true) {
            if(connected) {
                try {
                    if (commands.size() > 0) {
                        debug("Command sending: " + commands.get(commands.size() - 1));
                        dOut.writeUTF(commands.get(commands.size() - 1));
                        commands.remove(commands.size() - 1);
                    }
                    answer = dIn.readUTF();
                    handleIncomingPacket(answer);
                    if (!answer.equals("Listening..."))
                        debug(dIn.readUTF());


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

    public void handleIncomingPacket(String s) { 
    	switch (s) {
    	
	    	case Packets.P_INSETUP: 
	    		Global.setBoardState(Global.BoardState.SETUP);
	    		Global.setGameState(GameState.GAME);
	    		System.out.println("Set gamestate");
	    		break;
	    	case Packets.P_INGAME:
	    		Global.setGameState(Global.GameState.GAME);
	    	default: 
	    		break;
    	}
    }
    public String getAnswer() {
    	return answer;
    }

    /**
     * Send commands to the server using this
     *
     * @param s Packet you want sent to server
     */
    public void addCommand(String s) {
        commands.add(s);
        debug("Added command: " + s + " | Size: " + commands.size());
    }

    /**
     * remove commands from queue to the server using this
     *
     * @param s Packet you want to remove
     */
    public void removeCommand(String s) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).equals(s)) {
                debug("Removing command: " + s);
                commands.remove(i);
            }
        }
    }
}
