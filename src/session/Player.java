package session;

import java.util.ArrayList;

import game.Piece;

public class Player {

		public ArrayList<Piece> pieces;
		public boolean isTurn;
		
		public Player (ArrayList<Piece> playerPieces)  {
			pieces = playerPieces;
		}
		
		public Player () {
			pieces = new ArrayList<Piece>();
			
			Piece p1 = new Piece(Piece.PieceType.SCOUT).setPosition(0, 0);
			Piece p2 = new Piece(Piece.PieceType.SCOUT).setPosition(1, 1);
			Piece p3 = new Piece(Piece.PieceType.SCOUT).setPosition(0, 1);
			Piece p4 = new Piece(Piece.PieceType.SCOUT).setPosition(2, 6);
			Piece p5 = new Piece(Piece.PieceType.SCOUT).setPosition(3, 4);
			
			pieces.add(p1);
			pieces.add(p2);
			pieces.add(p3);
			pieces.add(p4);
			pieces.add(p5);

		}
		
		public Piece getPieceByPos(int col, int row) {
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i).getColumn() == col) 
					if (pieces.get(i).getRow() == row)
						return pieces.get(i);
			}
			return null;
		}
		
		public ArrayList<Piece> getPlayerPieces() {
			return pieces;
		}
		
		public void setPlayerPieces(ArrayList<Piece> p) {
			pieces = p;
		}
		
		public void setTurn(boolean t) {
			isTurn = t;
		}
		
		public boolean isTurn() {
			return isTurn;
		}
}
