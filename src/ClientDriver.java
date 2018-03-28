import javax.swing.*;

import client.Global;
import client.Network;
import client.SPanel;
import client.resources.Images;
import client.resources.Sound;
import game.Board;
import game.Piece;
import server.Server;
import server.ServerWindow;

public class ClientDriver extends JFrame {

    //Server-Client communication
    public static int PORT = 22344;
    public static String SERVER = "localhost";

    public ClientDriver() {
      //  Global.setGameState(Global.GameState.GAME);
        Global.setBoardState(Global.BoardState.SETUP);
        Board.initialize();
        Images.initialize();
        Sound.initialize();
        add(new SPanel());
        setTitle("Stratego 2442");
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Global.WIDTH, Global.HEIGHT);
        setVisible(true);

    }

    public static void main(String[] args) {

		
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				 new ClientDriver();
                 Global.connectedServer = new Network(PORT, SERVER);
				 new Thread(Global.connectedServer).start();

			}
		});
    }


}
