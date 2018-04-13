package game;


import client.Global;
import game.player.AIPlayer;
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

        Piece dPiece = Board.getCurrentOpposingPlayer().getPiece(zPiece);
        Animation.playAnimation(dPiece.getColumn(), dPiece.getRow(), dPiece.getPieceType().getSpriteIndex());

        if (dPiece.getPieceType().isPieceSpecial()) {
            switch (dPiece.getPieceType()) {
                case BOMB:
                    if (aPiece.getPieceType().equals(Piece.PieceType.BOMB_DEFUSER)) {
                        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), aPiece.clone());
                        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                        Board.addCapturedPiece(dPiece.getPieceType());
                        if(!Global.isNetworkGame()&&Board.getCurrentOpposingPlayer().equals(Board.getEnemyPlayer()))
                				AIPlayer.updatetracker(aPiece,dPiece,AIPlayer.moveUpdate.ATTACKWIN);
                        else if(!Global.isNetworkGame()&&Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
                    			AIPlayer.updatetracker(dPiece,dPiece,AIPlayer.moveUpdate.ATTACKLOSS);
                    } else {
                        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                        Board.addLostPiece(aPiece.getPieceType());
                        if(!Global.isNetworkGame()&&Board.getCurrentOpposingPlayer().equals(Board.getEnemyPlayer()))
            					AIPlayer.updatetracker(aPiece,aPiece,AIPlayer.moveUpdate.ATTACKLOSS);
                        else if(!Global.isNetworkGame()&&Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
                				AIPlayer.updatetracker(dPiece,dPiece,AIPlayer.moveUpdate.ATTACKWIN);
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
                Board.getCurrentPlayer().removePiece(aPiece);
                Board.getCurrentOpposingPlayer().removePiece(dPiece);
                System.out.println("Attacked an opponent with the same number");
                if(!Global.isNetworkGame()&&Board.getCurrentOpposingPlayer().equals(Board.getEnemyPlayer()))
                		AIPlayer.updatetracker(aPiece,aPiece,AIPlayer.moveUpdate.TIE);
                else if(!Global.isNetworkGame()&&Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
        				AIPlayer.updatetracker(dPiece,dPiece,AIPlayer.moveUpdate.TIE);
            } else if (aPiece.getPieceType().getCombatValue() > dPiece.getPieceType().getCombatValue()) {
                System.out.println("Attacked an opponent with a lower number");
                Board.getCurrentPlayer().removePiece(aPiece);
                if(!Global.isNetworkGame()&&Board.getCurrentOpposingPlayer().equals(Board.getEnemyPlayer())) {
            			AIPlayer.updatetracker(aPiece,aPiece,AIPlayer.moveUpdate.ATTACKLOSS);
                }
                else if(!Global.isNetworkGame()&&Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
        				AIPlayer.updatetracker(dPiece,dPiece,AIPlayer.moveUpdate.ATTACKWIN);


            } else {
                System.out.println("Attacked an opponent with a higher number");
                Board.getCurrentOpposingPlayer().removePiece(dPiece);
                if(Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
                		Board.getCurrentPlayer().movePiece(aPiece, dPiece);
                else
                		Board.getCurrentPlayer().movePiece(dPiece, aPiece);
                
                
                if(!Global.isNetworkGame()&&Board.getCurrentOpposingPlayer().equals(Board.getEnemyPlayer())) 
                		AIPlayer.updatetracker(aPiece,dPiece,AIPlayer.moveUpdate.ATTACKWIN);
                else if(!Global.isNetworkGame()&&Board.getCurrentPlayer().equals(Board.getEnemyPlayer()))
    					AIPlayer.updatetracker(dPiece,dPiece,AIPlayer.moveUpdate.ATTACKLOSS);
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

                        Piece p = Board.getPiece(dx, dy);

                         if (p.getPieceType().equals(Piece.PieceType.EMPTY) || Board.getCurrentOpposingPlayer().hasPiece(p))
                                pieces.add(p);

                        if (!p.getPieceType().equals(Piece.PieceType.EMPTY))
                            break;

                    }

                }
            }
        }
        return pieces;
    }

}
