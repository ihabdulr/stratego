package session;

import game.SaveLoad;
import server.Packets;
import server.SocketHandler;

public class SessionLoop implements Runnable {

	private SocketHandler player1, player2;

	public SessionLoop(SocketHandler p1, SocketHandler p2) {
		player1 = p1;
		player2 = p2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		//System.out.println("Player 1 : " + player1.getAddress() + " | " + player1.getPlayer().getStatus().toString());
		//System.out.println("Player 2 : " + player2.getAddress() + " | " + player2.getPlayer().getStatus().toString());
		//gameLoop();
		if (player1.getPlayer().getStatus().equals(Player.Status.MAIN_MENU))
			player1.sendPacketToClient(Packets.P_INSETUP);

		if (player2.getPlayer().getStatus().equals(Player.Status.MAIN_MENU))
			player2.sendPacketToClient(Packets.P_INSETUP);

		if (player1.getPlayer().getStatus().equals(Player.Status.READY))
			if (player2.getPlayer().getStatus().equals(Player.Status.READY)) {
				//System.out.println("Player 1 and 2 are ready, sending into game.");
				player1.sendPacketToClient(Packets.P_GIVING_PIECES + Packets.P_SEPERATOR + SaveLoad.getPiecesAsString(player2.getPlayer().getPlayerPieces()));
				player2.sendPacketToClient(Packets.P_GIVING_PIECES + Packets.P_SEPERATOR + SaveLoad.getPiecesAsString(player1.getPlayer().getPlayerPieces()));

				player1.sendPacketToClient(Packets.P_INGAME);
				player2.sendPacketToClient(Packets.P_INGAME);
			}

	
	}

	public void gameLoop() {
		System.out.println("Player 1 Pieces: " + SaveLoad.getPiecesAsString(player1.getPlayer().getPlayerPieces()));
		System.out.println("Player 2 Pieces: " + SaveLoad.getPiecesAsString(player2.getPlayer().getPlayerPieces()));

	}

}
