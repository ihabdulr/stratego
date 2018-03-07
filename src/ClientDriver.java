import javax.swing.*;

import client.Global;
import client.Network;
import client.SPanel;
import client.resources.Images;
import game.Board;
import game.Piece;

public class ClientDriver extends JFrame {

    //Server-Client communication
    public static int PORT = 22344;
    public static String SERVER = "localhost";
    public static Network mainClient;


    public ClientDriver() {
        Global.setGameState(Global.GameState.GAME);
        Board.initialize();
        Images.initialize();
        Board.setPiece(0, 0, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(1, 1, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(0, 1, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(2, 6, new Piece(Piece.PieceType.GENERIC));
        Board.setPiece(3, 4, new Piece(Piece.PieceType.SCOUT));
        Board.setPiece(4, 4, new Piece(Piece.PieceType.SCOUT));
        Board.setPiece(3, 5, new Piece(Piece.PieceType.KING));
        add(new SPanel());
        setTitle("Stratego 2442");
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setVisible(true);

    }

    public static void main(String[] args) {
        //mainClient = new Network(PORT, SERVER);
        new ClientDriver();
    }


}
