package game.player;

import game.Board;
import game.Piece;
import game.SaveLoad;
import server.Packets;

import java.util.ArrayList;
import java.util.Optional;

import client.Global;

/**
 * Created by Alek on 3/23/2018.
 */
public class NetworkPlayer extends GamePlayer {
	public java.util.List<Piece> pieces = null; 
	@Override
	public boolean hasAtLeastOneMovablePiece() {
		return true; //for now
	}

    @Override
    public Optional<Piece> getPiece(int x, int y) {
   
        //TODO write your code here
        //serverRequest =
        Piece serverRequest1 = null;
    	if (Global.connectedServer.isConnected()) {
    		String xx = Integer.toString(x);
    		String yy = Integer.toString(y);
    		Global.connectedServer.sendPacketToServer(Packets.P_REQUEST_PIECE + Packets.P_SEPERATOR + xx + Packets.P_SEPERATOR + yy);
    		serverRequest1 =  SaveLoad.convertPiece(Global.connectedServer.getAnswer());
    	}
        return serverRequest1 == null ? Optional.empty() : Optional.of(serverRequest1);

    }
	@Override
	public boolean removePiece(Piece piece) {
		return true;
	}

	@Override
	public boolean movePiece(Piece dPiece, Piece aPiece) {


        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), dPiece.setPieceType(Piece.PieceType.GENERIC));
        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(aPiece.getPosition())).findAny();
        if (p.isPresent()) {
            myPieces.get(myPieces.indexOf(p.get())).setPosition(dPiece.getColumn(), dPiece.getRow());
            return true;
        }
        System.out.println("Failed to move " + dPiece.getPieceType().name() + " to " + aPiece.getPosition().toString());
        return false;
	}
	
	public void setPieces(java.util.List<Piece> ps) {
		pieces = ps;
	}
	@Override
	public java.util.List<Piece> getSanitizedPieces() {
			if (pieces == null) {
				//Global.connectedServer.sendPacketToServer(Packets.P_GET_PIECES);
				System.out.println("Network player pieces still null");
				return null;
			}
	        java.util.List<Piece> returnPieces = new ArrayList<>();
	        pieces.forEach(i -> {
	            returnPieces.add(new Piece(Piece.PieceType.GENERIC).setPosition(i.getPosition()));
	        });
	        return returnPieces;
	}

}
