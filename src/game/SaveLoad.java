package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class SaveLoad {


    public static String getPiecesAsString() {
        String playerSetup = "";
        for (int x = 0; x < Board.getLocalPlayer().getPieces().size(); ++x) {
            Piece p = Board.getLocalPlayer().getPieces().get(x);
            if (p.getPieceType().equals(Piece.PieceType.EMPTY))
                continue;
            playerSetup += p.getPieceType().name() + ":" + p.getColumn() + ":" + p.getRow() + ",";
        }
        return playerSetup;
    }
    
    public static String getBoardAsString(Piece[][] pp) {
    	String playerSetup = "";
    	for (int c = 0; c < pp.length; c++) {
    		for (int r = 0; r < pp.length; r++) {
    			Piece p = Board.getPiece(c, r);
    			playerSetup += getPieceAsString(p); 
    		}
    	}
    	return playerSetup;
    }
    
    public static String getPiecesAsString(java.util.List pieces) {
        String playerSetup = "";
        for (int x = 0; x < pieces.size(); ++x) {
            Piece p = (Piece) pieces.get(x);
            if (p.getPieceType().equals(Piece.PieceType.EMPTY))
                continue;
            playerSetup += p.getPieceType().name() + ":" + p.getColumn() + ":" + p.getRow() + ",";
        }
        return playerSetup;
    }

    public static String getPieceAsString(Piece p) {
    	return p.getPieceType().name() + ":" + p.getColumn() + ":" + p.getRow();
    }
    
    public static void saveSetup() {
        String playerSetup = getPiecesAsString();

        playerSetup += " ";
        try {
            BufferedWriter text = new BufferedWriter(new FileWriter(new File("PlayerSetup")));
            text.write(playerSetup);
            text.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static Piece convertPiece(String pieceString) {
    	String[] props = pieceString.split(":");
        int col = Integer.parseInt(props[1]);
        int row = Integer.parseInt(props[2]);
        String pieceName = props[0];
        Piece.PieceType pieceType = null;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            if (pieceName.equals(type.name())) {
                pieceType = type;
                break;
            }
        }
        Piece boardPiece = new Piece(pieceType).setPosition(col, row);
        return boardPiece;
    }
    public static java.util.List<Piece> convertPieces(String piecesString) {
        java.util.List<Piece> pieceList = new ArrayList<>();
        String[] pieces = piecesString.split(",");
        for (String piece : pieces) {
            if (piece.length() < 2)
                break;
            String[] props = piece.split(":");
            int col = Integer.parseInt(props[1]);
            int row = Integer.parseInt(props[2]);
            String pieceName = props[0];
            Piece.PieceType pieceType = null;
            for (Piece.PieceType type : Piece.PieceType.values()) {
                if (pieceName.equals(type.name())) {
                    pieceType = type;
                    break;
                }
            }
            Piece boardPiece = new Piece(pieceType).setPosition(col, row);
            pieceList.add(boardPiece);

        }
        return pieceList;
    }

    
    public static java.util.List<Piece> convertPiecesWithFlip(String piecesString) {
        java.util.List<Piece> pieceList = new ArrayList<>();
        String[] pieces = piecesString.split(",");
        for (String piece : pieces) {
            if (piece.length() < 2)
                break;
            String[] props = piece.split(":");
            int col = Integer.parseInt(props[1]);
            int row = Integer.parseInt(props[2]);
            //System.out.println("old row = "+row+ " and old col = " +col);
            int k = row + 1;
            row = (8 % k);
            
            int j = col;
            col = (j % 8);
           // System.out.println("new "+row+ " and new  col = " +col);

            //System.out.println("old row = "+ (k - 1)+ " , new row = "+row);
            //row = row % 8;
            String pieceName = props[0];
            Piece.PieceType pieceType = null;
            for (Piece.PieceType type : Piece.PieceType.values()) {
                if (pieceName.equals(type.name())) {
                    pieceType = type;
                    break;
                }
            }
            Piece boardPiece = new Piece(pieceType).setPosition(col, row);
            pieceList.add(boardPiece);

        }
        return pieceList;
    }

    public static boolean stateFileExists() {
        return new File("PlayerSetup").exists();
    }

    public static void loadSetup() {

        BufferedReader textReader = null;
        try {
            textReader = new BufferedReader(new FileReader(new File("PlayerSetup")));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            for (Piece piece : convertPieces(textReader.readLine()))
                Board.setPiece(piece.getColumn(), piece.getRow(), piece);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
