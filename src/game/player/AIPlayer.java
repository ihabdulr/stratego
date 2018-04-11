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
        //System.out.println("Removing AI Piece: " + myPieces.indexOf(piece));
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(piece.getPosition())).findAny();
        if(p.isPresent()) {
            Board.setPiece(piece.getColumn(), piece.getRow(), new Piece(Piece.PieceType.EMPTY));
            return myPieces.remove(p.get());
        } else {
            System.out.println("Error finding piece in AI");
            return false;
        }
    }

    @Override
    public boolean movePiece(Piece aPiece, Piece dPiece) {
        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), aPiece.clone());
        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(aPiece.getPosition())).findAny();
        if(p.isPresent()) {
            System.out.println("good");
            myPieces.get(myPieces.indexOf(p.get())).setPosition(dPiece.getColumn(), dPiece.getRow());
            return true;
        }
        System.out.println("Failed to move " +dPiece.getPieceType().name() +" to " + aPiece.getPosition().toString());
        return false;
    }


    public boolean nextMove() {
        for (Piece piece : myPieces) {
            java.util.List<Piece> available = GameLogic.getMovableTiles(piece);
            //System.out.println(piece.getPieceType().name() +", available: " + available.size());
            if (!available.isEmpty()) {
                Board.move(piece, available.get(0));
                Global.setBoardState(Global.BoardState.MY_TURN);
                System.out.println("Enemy moving: " + piece.getPosition() + " to " + available.get(0).getPosition());
                return true;
            }
        }
        Global.setBoardState(Global.BoardState.GAME_WON); //should use hasatleastonemovablepiece later
        return false;
    }


}
