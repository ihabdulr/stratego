package game.player;

import game.Board;
import game.ConditionalSleep;
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
	public java.util.List<Piece> pieces = new ArrayList<Piece>(); 
	public Optional<Piece> askedPiece = null; 
	@Override
	public boolean hasAtLeastOneMovablePiece() {
		return true; //for now
	}

    @Override
    public Optional<Piece> getPiece(int x, int y) {
   
  
    	if (Global.connectedServer.isConnected()) {
    		String xx = Integer.toString(x);
    		String yy = Integer.toString(y);
    		System.out.println("Asking for ("+xx+", "+yy+")");

    		Global.connectedServer.sendPacketToServer(Packets.P_REQUEST_PIECE + Packets.P_SEPERATOR + xx + Packets.P_SEPERATOR + yy);
            Thread t = new Thread(new ConditionalSleep(2000) {
            	
            	public Optional<Piece> ap = null;
				@Override
				public boolean condition() {
					// TODO Auto-generated method stub
					return (ap != null);
				}

				@Override
				public void call() {
					// TODO Auto-generated method stub
					
				}
            });
            t.start();

            if (askedPiece != null) {
            	Optional<Piece> temp  = (askedPiece);
            	
    			askedPiece = null;
    			return temp;
            }
    	}
        return Optional.empty() ;

    }
    
    public void setAsked(String s) {
    	Piece p = SaveLoad.convertPiece(s);
    	askedPiece = p == null ? Optional.empty() : Optional.of(p);
    }
    
	@Override
	public boolean removePiece(Piece piece) {
		return true;
	}

    @Override
    public boolean movePiece(Piece aPiece, Piece dPiece) {
        System.out.println("attempting to move " + dPiece.getPieceType().name() + " to " + aPiece.getPosition().toString());

        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), dPiece.setPieceType(Piece.PieceType.GENERIC));
        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
        Optional<Piece> p = pieces.stream().filter(i -> i.getPosition().equals(aPiece.getPosition())).findAny();
        if (p.isPresent()) {
            pieces.get(pieces.indexOf(p.get())).setPosition(dPiece.getColumn(), dPiece.getRow());
            return true;
        }
        System.out.println("Failed to move " + dPiece.getPieceType().name() + " to " + aPiece.getPosition().toString());
        return false;
    }

	@Override
    public java.util.List<Piece> getPieces() {
        return pieces;
    }
	
	
	
	public void setPieces(java.util.List<Piece> ps) {
		pieces.clear();
		pieces = ps;
	}
	
	public java.util.List<Piece> getSanitizedPieces() {			
	        java.util.List<Piece> returnPieces = new ArrayList<>();
	        pieces.forEach(i -> {
	            returnPieces.add(new Piece(Piece.PieceType.GENERIC).setPosition(i.getPosition()));
	        });
	        return returnPieces;
	}

}
