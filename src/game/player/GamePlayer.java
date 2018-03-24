package game.player;

import game.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by Alek on 3/23/2018.
 */
public abstract class GamePlayer {


    protected java.util.List<Piece> myPieces = new ArrayList<>();

    public abstract java.util.List<Piece> getPieces();

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
