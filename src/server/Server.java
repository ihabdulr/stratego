package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame; 



public class Server implements Runnable {

	/** PORT and TIMEOUT for current server **/
	public int PORT, TIMEOUT;
	
	/** Debug server into console **/
	public boolean DEBUG = true;
	
	public ServerSocket mainServer;
	
	public ServerWindow mainFrame;
	
	/** Hashmap of all connections */
	public HashMap<Integer, SocketHandler> connections = new HashMap<Integer, SocketHandler>();
	
	public Server(int port, int timeout) {
		try {
			PORT = port;
			TIMEOUT = timeout;
			
			mainServer = new ServerSocket(port);
			mainServer.setSoTimeout(timeout);
			//SocketHandler test1 = new SocketHandler(null, getNumberOfConnections() + 1, this);
			//connections.put(1, test1);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFrame(ServerWindow f) {
		mainFrame = f;
	}
	
	public void debug(String s) {
		if (DEBUG) {
			System.out.println("[SERVER]: "+s);
			mainFrame.writeToConsole("[SERVER]: "+s);
		}
	}
	
	public HashMap<Integer, SocketHandler> getClients() {
		return connections;
	}
	
	public int getNumberOfConnections() {
		return connections.size();
	}
	
	public void addClient(SocketHandler h) {
		connections.put(getNumberOfConnections() + 1, h);
		h.start();
		mainFrame.updateTable();
	}
	
	@Override
	public void run() {
		
		while (true) {
			try {

				debug("Waiting on incoming connections on port: "+PORT+" at "+mainServer.getInetAddress());
				Socket socket = mainServer.accept();
				
				debug("Connection established at "+socket.getRemoteSocketAddress()+ ", sending to socket handler");


				SocketHandler s = new SocketHandler(socket, getNumberOfConnections() + 1, this);
				addClient(s);
				if (s.getSocket().isClosed()) {
					decrementClientCount(s.id);
					
				}
			
			}catch (SocketTimeoutException s) {
		            debug("Socket timed out after "+(TIMEOUT / 1000)+ " seconds");
		            break;
			
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} 
			
		
		}
	}	
	
	public void decrementClientCount(int id) {
	
		connections.remove(id);
		mainFrame.updateTable();

		debug("A client has disconnected, total connected clients: "+getNumberOfConnections());
	}
	
	
	
}
