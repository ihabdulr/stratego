package game.player;

import game.GameLogic;
import game.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Created by Alek on 3/23/2018.
 */
public abstract class GamePlayer {


    protected java.util.List<Piece> myPieces = new ArrayList<>();

    public java.util.List<Piece> getPieces() {
        return myPieces;
    }


    public abstract boolean hasAtLeastOneMovablePiece();

    public void movePiece(Piece piece, Point destination) {
       for(Piece p: myPieces) {
           if(p.getPieceType().equals(piece.getPieceType()) && p.getPosition().equals(piece.getPosition())) {
               myPieces.set(myPieces.indexOf(p), p.clone().setPosition(destination)); //lets be safe
               return;

           }
       }
    }

    public java.util.List<Piece> getSanitizedPieces() {
        java.util.List<Piece> returnPieces = new ArrayList<>();
        myPieces.forEach(i -> {
            returnPieces.add(new Piece(Piece.PieceType.GENERIC).setPosition(i.getPosition()));
        });
        return returnPieces;
    }

    public Optional<Piece> getPiece(int x, int y) {
        Optional<Piece> piece = myPieces.stream().filter(i -> i.getColumn() == x && i.getRow() == y).findFirst();
        if (piece.isPresent()) {
            return piece;
        }
        return null;
    }

    public Optional<Piece> getPiece(Point point) {
        return getPiece(point.x, point.y);
    }

}
