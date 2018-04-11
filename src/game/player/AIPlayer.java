package game.player;

import client.Global;
import game.Board;
import game.GameLogic;
import game.Piece;
import game.SetupContainer;

import java.util.Collections;
import java.util.Optional;

import static game.Board.SIZE_X;

/**
 * Created by Alek on 3/23/2018.
 */
public class AIPlayer extends GamePlayer {


    public AIPlayer() {
        this(1);
    }

    public AIPlayer(int mode) {
        if (mode == 0) {
            java.util.List<Piece.PieceType> pieces = SetupContainer.getGamePieces();
            Collections.shuffle(pieces);
            int flatMap = pieces.size();
            for (int x = 0; x < SIZE_X; ++x) {
                for (int y = 0; y < 3; ++y) {
                    myPieces.add(new Piece(pieces.get(flatMap - 1)).setPosition(x, y));
                    --flatMap;
                }
            }
        } else if (mode == 1) {
            myPieces.add(new Piece(Piece.PieceType.FLAG).setPosition(0, 3));
            myPieces.add(new Piece(Piece.PieceType.BOMB).setPosition(1, 3));
            myPieces.add(new Piece(Piece.PieceType.MINION).setPosition(0, 0));
        }


    }

    @Override
    public boolean hasAtLeastOneMovablePiece() {
        return getPieces().stream().anyMatch(p -> !GameLogic.getMovableTiles(p).isEmpty());
    }

    @Override
    public boolean removePiece(Piece piece) {
        System.out.println("Removing AI Piece: " + piece.getPieceType().name());
        Board.addCapturedPiece(piece.getPieceType());
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(piece.getPosition())).findAny();
        if (p.isPresent()) {
            Board.setPiece(piece.getColumn(), piece.getRow(), new Piece(Piece.PieceType.EMPTY));
            System.out.println("Size: " + myPieces.size());
            myPieces.remove(p.get());
            System.out.println("Size: " + myPieces.size());

            System.out.println("f: " + hasAtLeastOneMovablePiece());
            return true;
        } else {
            System.out.println("Error finding piece in AI");
            return false;
        }
    }

    @Override
    public boolean movePiece(Piece aPiece, Piece dPiece) {
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


    public boolean nextMove() {
        for (Piece piece : myPieces) {
            java.util.List<Piece> available = GameLogic.getMovableTiles(piece);
            if (!available.isEmpty()) {
                Board.TurnState state = Board.move(piece, available.get(0));
                System.out.println("Enemy moving: " + piece.getPosition() + " to " + available.get(0).getPosition());
                if(state.equals(Board.TurnState.VALID)) {
                    Global.setBoardState(Global.BoardState.MY_TURN);
                    return true;
                }
                else
                    Global.setBoardState(Global.BoardState.GAME_WON);

            }
        }
        Global.setBoardState(Global.BoardState.GAME_WON);
        return false;
    }


}
