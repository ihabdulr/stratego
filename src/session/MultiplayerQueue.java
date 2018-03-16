package session;

import java.util.ArrayList;

public class MultiplayerQueue {
	
	public ArrayList<Player> playerQueue = null;
	public ArrayList<GameSession> currentGames = null;
	
	public MultiplayerQueue() {
		playerQueue = new ArrayList<Player>();
		currentGames = new ArrayList<GameSession>();
	}
	
	
	public void addPlayer(Player p) {
		playerQueue.add(p);
		System.out.println("Added player to queue, queue count is now: "+playerQueue.size());
	}
	
	public void removePlayer(int index) {
		playerQueue.remove(index);
		System.out.println("removed player from queue, queue count is now: "+playerQueue.size());

	}
	
	public void removeGame(int index) { 
		currentGames.remove(index);
	}
	
	public ArrayList<GameSession> getGames() {
		return currentGames;
	}
	
	public void checkForMatch() {
		if (playerQueue.size() > 1) {
			Player a = playerQueue.get(0);
			Player b = playerQueue.get(1);
			removePlayer(0);
			removePlayer(1);
			currentGames.add(new GameSession(a, b, currentGames.size() + 1));
		}
		
	}
}
