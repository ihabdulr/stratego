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

	public java.util.List<Piece> pieces;
	public String clientAddress; //if empty, not playing multiplayer
	public boolean ready;

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
		SaveLoad.convertPieces(s);
	}

	public void setReady(boolean t) {
		ready = t;
	}

	public boolean isReady() {
		return ready;
	}

}
