package game.player;

import client.Global;
import game.Board;
import game.GameLogic;
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


    public boolean nextMove() {
        for(Piece piece : myPieces) {
            java.util.List<Piece> available = GameLogic.getMovableTiles(piece);
            //System.out.println(piece.getPieceType().name() +", available: " + available.size());
            if(!available.isEmpty()) {
                Board.move(piece, available.get(0));
                Global.setBoardState(Global.BoardState.MY_TURN);
                System.out.println("Enemy moving: " + piece.getPosition() + " to " + available.get(0).getPosition());
                return true;
            }
        }
        System.out.println("Enemy does not have any moves available!");
        return false;
    }



}
