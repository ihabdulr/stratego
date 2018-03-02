package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Setups the client to attempt an connect to a server
 * @author Aaron Roberts
 *
 */

public class Network {

	public int PORT;
	public boolean DEBUG = true;

	public String SERVER;

	public Network(int port, String server) {
		PORT = port;
		SERVER = server;
		connectToServer();
	}

	public void debug(String s) {
		if (DEBUG) {
			String timeStamp = new SimpleDateFormat("hh:mm:ss").format(new Date());

			System.out.println("\t[" + timeStamp + "]: " + s);
		}
	}

	public void connectToServer() {
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
				/** This is just used for testing purposes, ideally we will
				keep the client running until the client either is closed or requests "DESTROY"
				**/
				for (int i = 0; i < 4; i++) {
					if (dOut != null) {
						if (i == 0)
							dOut.writeUTF("PING");
						if (i == 1)
							dOut.writeUTF("REQUESTID");
						if (i == 2)
							dOut.writeUTF("REQUESTADDRESS");
						
						debug(dIn.readUTF());
						dOut.writeUTF("randomRequest"+i+"error");
						debug(dIn.readUTF());

						// dOut.writeUTF("REQUESTID");
					}
				}
				//dOut.writeUTF("DESTROY");
				debug(dIn.readUTF());
				//clientConnection.close();
				//System.out.println("Connection to server destroyed");
				return;
			} catch (SocketException e) { 	 
	             return;
           } catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
