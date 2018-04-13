package game.player;

import game.Board;
import game.GameLogic;
import game.Piece;


import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Alek on 3/24/2018.
 */
public class LocalPlayer extends GamePlayer {

    @Override
    public java.util.List<Piece> getPieces() {
        return Stream.of(Board.getPieces()).flatMap(Stream::of).filter(i ->
                i.getPieceType().isSelectable() ||
                        i.getPieceType().isPieceSpecial()
        ).collect(Collectors.toList());
    }

    @Override
    public boolean hasAtLeastOneMovablePiece() {
        return getPieces().stream().anyMatch(p -> !GameLogic.getMovableTiles(p).isEmpty());
    }

    @Override
    public boolean removePiece(Piece piece) {
        System.out.println("Removing Local Piece");
        Board.setPiece(piece.getColumn(), piece.getRow(), new Piece(Piece.PieceType.EMPTY));
        Board.addLostPiece(piece.getPieceType());
        return true;
    }

    @Override
    public boolean movePiece(Piece dPiece, Piece aPiece) {
        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), aPiece.clone());
        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
        return true;
    }

}
