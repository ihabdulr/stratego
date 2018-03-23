package game.player;

import game.Piece;
import game.SetupContainer;

import java.util.Collections;

import static game.Board.SIZE_X;

/**
 * Created by Alek on 3/23/2018.
 */
public class AIPlayer extends GamePlayer {


    public AIPlayer() {
        java.util.List<Piece.PieceType> pieces = SetupContainer.getGamePieces();
        Collections.shuffle(pieces);
        int flatMap = pieces.size();
        for (int x = 0; x < SIZE_X; ++x) {
            for (int y = 0; y < 3; ++y) {
                myPieces.add(new Piece(pieces.get(flatMap - 1)).setPosition(x, y));
                --flatMap;
            }
        }
    }



}
