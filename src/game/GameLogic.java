package game;


import client.Global;
import game.player.GamePlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

import static game.Board.SIZE_X;
import static game.Board.SIZE_Y;

/**
 * Created by Alek on 3/14/2018.
 */
public class GameLogic {


    //Returns true if the flag was captured
    public static boolean attack(Piece aPiece, Piece zPiece) {

        /*
        Piece dPiece;

        //side 0 = aPiece = enemy, side 1 = dPiece = enemy
        int side = aPiece.getPieceType().equals(Piece.PieceType.GENERIC) ? 0 : 1;

        //Assume isPresent for now
        if (side == 0)
            dPiece = Board.getEnemyPlayer().getPiece(aPiece.getColumn(), aPiece.getRow()).get();
        else
            dPiece = Board.getEnemyPlayer().getPiece(zPiece.getColumn(), zPiece.getRow()).get();

        */

        Piece dPiece = Board.getEnemyPlayer().getPiece(zPiece.getPosition()).get();



        Animation.playAnimation(dPiece.getColumn(), dPiece.getRow(), dPiece.getPieceType().getSpriteIndex());


        if (dPiece.getPieceType().isPieceSpecial()) {
            switch (dPiece.getPieceType()) {
                case BOMB:
                    if (aPiece.getPieceType().equals(Piece.PieceType.BOMB_DEFUSER)) {
                        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), aPiece.clone());
                        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                        Board.addCapturedPiece(dPiece.getPieceType());
                    } else {
                        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                        Board.addLostPiece(aPiece.getPieceType());
                    }
                    break;
                case FLAG:
                    return true;

                default: //This shouldnt happen
                    System.out.println("Error: Reached end of game logic for special piece");
                    System.out.println("Piece 1: " + aPiece.getPieceType().name());
                    System.out.println("Piece 2: " + dPiece.getPieceType().name());
            }
        } else {
            if (aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {

                //Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                //Board.setPiece(dPiece.getColumn(), dPiece.getRow(), new Piece(Piece.PieceType.EMPTY));

                Board.getCurrentPlayer().removePiece(aPiece);
                Board.getCurrentOpposingPlayer().removePiece(aPiece);
                System.out.println("Attacked an opponent with the same number");

                Board.addCapturedPiece(dPiece.getPieceType());
                Board.addLostPiece(aPiece.getPieceType());
            } else if (aPiece.getPieceType().getCombatValue() > dPiece.getPieceType().getCombatValue()) {

                System.out.println("Attacked an opponent with a lower number");
                Board.getCurrentPlayer().removePiece(aPiece);
                //Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));

                Board.addLostPiece(aPiece.getPieceType());


            } else {
               // Board.setPiece(dPiece.getColumn(), dPiece.getRow(), aPiece.clone());
               // Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));

                System.out.println("Attacked an opponent with a higher number");
                Board.getCurrentOpposingPlayer().removePiece(dPiece);
                Board.getCurrentPlayer().movePiece(aPiece, dPiece);

                Board.addCapturedPiece(dPiece.getPieceType());
            }
        }
        return false;
    }

    //TODO two different pools of generic
    public static java.util.List<Piece> getMovableTiles(Piece piece) {
        java.util.List<Piece> pieces = new ArrayList<Piece>();
        if (piece != null) {
            if(piece.getPieceType().getCombatValue() == -1)
                return pieces;
            int mx = piece.getColumn();
            int my = piece.getRow();
            for (Board.Direction dir : Board.Direction.values()) {
                for (int i = 0; i < piece.getMaxDistance(); ++i) {
                    int dx = dir.equals(Board.Direction.LEFT) || dir.equals(Board.Direction.RIGHT) ? mx + (dir.getOffset() * (i + 1)) : mx;
                    int dy = dir.equals(Board.Direction.UP) || dir.equals(Board.Direction.DOWN) ? my + (dir.getOffset() * (i + 1)) : my;
                    if (dx >= 0 && dy >= 0 && dx < SIZE_X && dy < SIZE_Y) {

                        Piece p;
                        if (Global.getBoardState().equals(Global.BoardState.MY_TURN))
                            p = GameLogic.getPiece(new Point(dx, dy), Board.getLocalPlayer(), Board.getEnemyPlayer());
                        else
                            p = GameLogic.getPiece(new Point(dx, dy), Board.getEnemyPlayer(), Board.getLocalPlayer());

                        if (!p.getPieceType().equals(Piece.PieceType.BLOCK))
                            if (p.getPieceType().equals(Piece.PieceType.EMPTY) || p.getPieceType().equals(Piece.PieceType.GENERIC))
                                pieces.add(p);

                        if (!p.getPieceType().equals(Piece.PieceType.EMPTY)) {
                            // System.out.println(p.getPieceType().name());
                            break;
                        }
                    }

                }
            }
        }
        return pieces;
    }


    /*
    In this context, myPlayer is the player whose turn it is - not LocalPlayer
     */
    public static Piece getPiece(Point position, GamePlayer myPlayer, GamePlayer enemyPlayer) {

        java.util.List<Piece> pieces = new ArrayList<>();
        pieces.addAll(myPlayer.getPieces());
        pieces.addAll(enemyPlayer.getSanitizedPieces());

        Optional<Piece> returnPiece = pieces.stream().filter(p -> p.getPosition().equals(position)).findFirst();
        if (returnPiece.isPresent()) {
            return returnPiece.get();
        }
        //System.out.println(position.getX() +", " + position.getY());
        return new Piece(Piece.PieceType.EMPTY).setPosition(position);
    }


}
