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
        return Stream.of(Board.getPieces()).flatMap(Stream::of).filter(i -> !i.getPieceType().equals(Piece.PieceType.GENERIC)).collect(Collectors.toList());
    }

    @Override
    public boolean hasAtLeastOneMovablePiece() {
        return getPieces().stream().anyMatch(p -> !GameLogic.getMovableTiles(p).isEmpty());
    }

}
