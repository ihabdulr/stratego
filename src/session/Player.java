package session;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.Board;
import game.Piece;
import game.SaveLoad;


/**
 * 
 * @author Aaron Roberts
 *
 */

public class Player {

		private  java.util.List<Piece> pieces;
		private  String clientAddress; //if empty, not playing multiplayer
		private Status playerStatus = Status.MAIN_MENU;
		
		public enum Status
	    {
	        MAIN_MENU, SETUP, READY, IN_GAME;
	    }
		
		public Player () {
			

		}
		
		public Optional<Piece> getPiece(int x, int y) {
			Optional<Piece> piece = pieces.stream().filter(i -> i.getColumn() == x && i.getRow() == y).findFirst();
			if (piece.isPresent()) {
				return piece;
			}
			return null;
		}

		public void setClientAddress(String s) {
			clientAddress = s;
		}
		
		public String getAddress() {
			return clientAddress;
		}
		
		public java.util.List<Piece> getPlayerPieces() {
			return pieces;
		}
		
		public void setPlayerPieces(String s) {
			pieces = SaveLoad.convertPieces(s);
		}
		
		public void setStatus(Status p) {
			playerStatus = p;
		}
		
		public Status getStatus() {
			return playerStatus;
		}
		
		public void printPieces() {
			System.out.println("PLAYER PIECES: "+pieces);
		}
}
