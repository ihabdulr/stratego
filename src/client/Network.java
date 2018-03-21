package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Setups the client to attempt an connect to a server
 * @author Aaron Roberts
 *
 */

public class Network implements Runnable {

	public int PORT;
	public boolean DEBUG = true;

	public String SERVER;
	public ArrayList<String> commands;

	public Network(int port, String server) {
		PORT = port;
		SERVER = server;
		commands = new ArrayList<String>();
		
	}

	public void debug(String s) {
		if (DEBUG) {
			String timeStamp = new SimpleDateFormat("hh:mm:ss").format(new Date());

			System.out.println("\t[" + timeStamp + "]: " + s);
		}
	}


	@Override
	public void run() {
		
		DataInputStream dIn = null;
		DataOutputStream dOut = null;
		Socket clientConnection = null;
		try {
			System.out.println("Attempting to connect to " + SERVER + " on port " + PORT);
			clientConnection = new Socket(SERVER, PORT);
			System.out.println("Successfully connected. Sending message now");

			OutputStream out = clientConnection.getOutputStream();
			dOut = new DataOutputStream(out);

			InputStream in = clientConnection.getInputStream();
			dIn = new DataInputStream(in);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (clientConnection == null)
			return;
		while (true) {
			try {
				if (commands.size() > 0) {
					debug("Command sending: " +commands.get(commands.size() - 1));	
					dOut.writeUTF(commands.get(commands.size() - 1));	
					commands.remove(commands.size() - 1);
				} 
				String answer = dIn.readUTF();
				if (!answer.equals("Listening..."))
					debug(dIn.readUTF());
				

			} catch (SocketException e) { 	 
		         return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	
	/**
	 * Send commands to the server using this
	 * @param s Packet you want sent to server
	 */
	public void addCommand(String s) {
		commands.add(s);
		debug("Added command: "+s+ " | Size: "+commands.size());
	}
	
	/**
	 * remove commands from queue to the server using this
	 * @param s Packet you want to remove
	 */
	public void removeCommand(String s) {
		for (int i = 0; i < commands.size(); i++) {
			if (commands.get(i).equals(s)) {
					debug("Removing command: "+s);
					commands.remove(i);
			}
		}
	}
}
