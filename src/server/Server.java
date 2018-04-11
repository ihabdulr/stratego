package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import session.GameSession;
import session.MultiplayerQueue; 


/**
 * 
 * @author Aaron Roberts
 *
 */


public class Server implements Runnable {

	/** PORT and TIMEOUT for current server **/
	public int PORT, TIMEOUT;
	
	/** Debug server into console **/
	public boolean DEBUG = true;
	
	public ServerSocket mainServer;
	
	public ServerWindow mainFrame;
	
	public MultiplayerQueue multiplayerQueue = null;
	
	public ArrayList<GameSession> multiplayerGames = new ArrayList<GameSession> ();

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
			
				if (multiplayerQueue == null) {
					//debug("Initalized player queue");
					multiplayerQueue = new MultiplayerQueue();
					
					//Executor service to run a check for player matches every 5 seconds
					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.scheduleAtFixedRate(new Runnable() {
					  @Override
					  public void run() {
						  GameSession gs = multiplayerQueue.checkForMatch();
						  if (gs != null) {
							  multiplayerGames.add(gs);
							  
							  s.handlePacket(Packets.P_INSETUP, true);
						  }


						  for (int i = 0; i < multiplayerGames.size(); i++) {
							  GameSession bs = multiplayerGames.get(i);
							  if (bs.getPlayer1().isReady()) {
								  if (bs.getPlayer2().isReady()) {
									  //s.handlePacket(Packets.P_INSETUP, true);
									  bs.getPlayer1().setReady(false);
									  bs.getPlayer2().setReady(false);
								  }
							  }
						  }
					  }
					}, 0, 5, TimeUnit.SECONDS);
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
	
	public ArrayList<GameSession> getMultiplayerGames() {
		return multiplayerGames;
	}
	
	public void decrementClientCount(int id) {
	
		connections.remove(id);
		mainFrame.updateTable();

		debug("A client has disconnected, total connected clients: "+getNumberOfConnections());
	}
	
	
	
}
