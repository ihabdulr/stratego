package game.player;

import game.Piece;

import java.util.Optional;

/**
 * Created by Alek on 3/23/2018.
 */
public class NetworkPlayer extends GamePlayer {

    @Override
    public Optional<Piece> getPiece(int x, int y) {
        Piece serverRequest = null;
        //TODO write your code here
        //serverRequest =
        return serverRequest == null ? Optional.empty() : Optional.of(serverRequest);

    }

    @Override
    public java.util.List<Piece> getPieces() {
        return super.getSanitizedPieces();
    }

}
