package session;

/**
 * 
 * @author Aaron Roberts
 *
 */
public class GameSession {

	public Player player1, player2;
	public int gameIndex;
	
	public GameSession(Player p1, Player p2, int i) {
		System.out.println("FOUND A MATCH - Connected two game");
		player1 = p1;
		player2 = p2;
		gameIndex = i;
	}
}
