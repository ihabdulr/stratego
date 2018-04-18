package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.xml.internal.ws.api.message.Packet;

import client.Global;
import session.GameSession;
import session.MultiplayerQueue;
import session.Player;
import session.SessionLoop;

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
	public SessionLoop sessionLoop = null;

	public ArrayList<SessionLoop> multiplayerGames = new ArrayList<SessionLoop>();

	/** Hashmap of all connections */
	public HashMap<String, SocketHandler> connections = new HashMap<String, SocketHandler>();

	public HashMap<SocketHandler, SocketHandler> pairedClients = new HashMap<SocketHandler, SocketHandler>();

	public static ExecutorService executor = Executors.newFixedThreadPool(100);

	public Server(int port, int timeout) {
		try {
			PORT = port;
			TIMEOUT = timeout;

			mainServer = new ServerSocket(port);

			mainServer.setSoTimeout(timeout);
			// SocketHandler test1 = new SocketHandler(null, getNumberOfConnections() + 1,
			// this);
			// connections.put(1, test1);

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
			System.out.println("[SERVER]: " + s);
			mainFrame.writeToConsole("[SERVER]: " + s);
		}
	}

	public HashMap<String, SocketHandler> getClients() {
		return connections;
	}

	public int getNumberOfConnections() {
		return connections.size();
	}

	public SocketHandler addClient(SocketHandler h) {
		connections.put(h.getAddress(), h);

		mainFrame.updateTable();
		return h;
	}

	@Override
	public void run() {

		while (true) {
			try {

				debug("Waiting on incoming connections on port: " + PORT + " at " + mainServer.getInetAddress());
				Socket socket = mainServer.accept();

				debug("Connection established at " + socket.getRemoteSocketAddress() + ", sending to socket handler");

				// executor.execute( new SocketHandler(socket, getNumberOfConnections() + 1,
				// this) );
				Player pp = new Player();
				pp.setClientAddress(socket.getRemoteSocketAddress().toString());
				executor.submit(addClient(new SocketHandler(socket, getNumberOfConnections() + 1, this, pp, null)));
				// SocketHandler s = new SocketHandler(socket, getNumberOfConnections() + 1,
				// this);
				// addClient(s);

				if (multiplayerQueue == null)
					multiplayerQueue = new MultiplayerQueue();
				
			
				ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
				ScheduledExecutorService loops = Executors.newScheduledThreadPool(25);

				exec.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						GameSession gs = multiplayerQueue.checkForMatch();
						if (gs != null) {
							debug("Game session: Player 1: " + gs.getPlayer1().getAddress() + " | Player 2: "
									+ gs.getPlayer2().getAddress() + " | Index : " + gs.getGameIndex());
							if (gs.getPlayer1().getAddress().equals(socket.getRemoteSocketAddress().toString())) {
								connections.get(gs.getPlayer1().getAddress()).setOpponent(gs.getPlayer2());
								System.out.println("Set opponent for p1");

							}
							if (gs.getPlayer2().getAddress().equals(socket.getRemoteSocketAddress().toString())) {
								//Setting opponent player in socket handler
								connections.get(gs.getPlayer1().getAddress()).setOpponent(gs.getPlayer1());
								System.out.println("Set opponent for p2");
							}
							pairedClients.put(connections.get(gs.getPlayer1().getAddress()),
									connections.get(gs.getPlayer2().getAddress()));
							SessionLoop sl = new SessionLoop(connections.get(gs.getPlayer1().getAddress()), connections.get(gs.getPlayer2().getAddress()));
							
							loops.scheduleAtFixedRate(sl, 0, 3, TimeUnit.SECONDS);
							multiplayerGames.add(sl);
							
						}
					}
				}, 0, 5, TimeUnit.SECONDS); 

				debug("Game sessions size: "+pairedClients.size());
				/*for (HashMap.Entry<SocketHandler, SocketHandler> entry : pairedClients.entrySet()) {
					SocketHandler s1 = entry.getKey();

					SocketHandler s2 = entry.getValue();

					// do stuff

					debug("Player 1 : " + s1.getAddress() + " | " + s1.getPlayer().getStatus().toString());
					debug("Player 2 : " + s2.getAddress() + " | " + s1.getPlayer().getStatus().toString());
					
					if (s1.getPlayer().getStatus().equals(Player.Status.MAIN_MENU)) 
						s1.sendPacketToClient(Packets.P_INSETUP);
					
					if (s2.getPlayer().getStatus().equals(Player.Status.MAIN_MENU)) 
						s2.sendPacketToClient(Packets.P_INSETUP);
					
					if (s1.getPlayer().getStatus().equals(Player.Status.READY)) 
						if (s2.getPlayer().getStatus().equals(Player.Status.READY)) {
							debug("Player 1 and 2 are ready, sending into game.");
							s1.sendPacketToClient(Packets.P_INGAME);
							s2.sendPacketToClient(Packets.P_INGAME);
						}

					
				}*/

			} catch (SocketTimeoutException s) {
				debug("Socket timed out after " + (TIMEOUT / 1000) + " seconds");
				break;

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

		}
	}

	public MultiplayerQueue getMultiplayerQueue() {
		return multiplayerQueue;
	}
	
	public HashMap<SocketHandler, SocketHandler> getPairedClients() {
		return pairedClients;
	}

	public void decrementClientCount(String adress) {

		connections.remove(adress);
		mainFrame.updateTable();

		debug("A client has disconnected, total connected clients: " + getNumberOfConnections());
	}

}
