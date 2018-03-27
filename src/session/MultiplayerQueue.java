package session;

import java.util.ArrayList;

/**
 * 
 * @author Aaron Roberts
 *
 */

public class MultiplayerQueue {
	
	public ArrayList<Player> playerQueue = null;
	public ArrayList<GameSession> currentGames = null;
	
	public MultiplayerQueue() {
		System.out.println("Initalizing player queue...");
		playerQueue = new ArrayList<Player>();
		currentGames = new ArrayList<GameSession>();
	}
	
	/**
	 * Adds player to multiplayer queue
	 * @param s Clients ip address
	 */
	public void addPlayer(String s) {
		Player m = new Player();
		m.setClientAddress(s);
		playerQueue.add(m);
		System.out.println("Added player to queue, queue count is now: "+playerQueue.size());
	}
	
	public void removePlayer(String s) {
		for (int i = 0; i < currentGames.size(); i++) {
			if (playerQueue.get(i).getAddress().equals(s)) {
				playerQueue.remove(i);
				System.out.println("removed player from queue, queue count is now: "+playerQueue.size());
				return;
			}
		}
		System.out.println("Couldn't find player");

	}
	
	public void removeGame(int index) { 
		currentGames.remove(index);
	}
	
	public ArrayList<GameSession> getGames() {
		return currentGames;
	}
	
	public ArrayList<Player> getPlayers() {
		return playerQueue;
	}
	
	
	public void checkForMatch() {
		//System.out.println("Queue count is now: "+playerQueue.size());
		if (playerQueue.size() > 1) {
			Player a = playerQueue.get(0);
			Player b = playerQueue.get(1);
			removePlayer(a.getAddress());
			removePlayer(b.getAddress());
			currentGames.add(new GameSession(a, b, currentGames.size() + 1));
			System.out.println("Players A and B are in a match! ");
		}
		
	}
}
