import javax.swing.*;

import client.Global;
import client.Network;
import client.SPanel;
import client.resources.Images;
import game.Board;
import game.Piece;
import server.Server;
import server.ServerWindow;

public class ClientDriver extends JFrame {

    //Server-Client communication
    public static int PORT = 22344;
    public static String SERVER = "localhost";
    public static Network mainClient;



    public ClientDriver() {
      //  Global.setGameState(Global.GameState.GAME);
        Global.setBoardState(Global.BoardState.SETUP);
        Board.initialize();
        Images.initialize();
        Board.setPiece(0, 0, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(1, 1, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(0, 1, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(2, 3, new Piece(Piece.PieceType.GENERIC));

        add(new SPanel());
        setTitle("Stratego 2442");
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Global.WIDTH, Global.HEIGHT);
        setVisible(true);

    }

    public static void main(String[] args) {
        mainClient = new Network(PORT, SERVER);
        
		
		Thread ms = new Thread(mainClient);
		
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				 new ClientDriver();
				 ms.start();
				 Global.setServer(mainClient);
			}
		});
    }


}
