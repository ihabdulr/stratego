package game;


import java.util.ArrayList;

import static game.Board.SIZE_X;
import static game.Board.SIZE_Y;

/**
 * Created by Alek on 3/14/2018.
 */
public class GameLogic {


    public static void attack(Piece aPiece, Piece zPiece) {

        Piece[][] pieces = Board.getPieces();

        Piece dPiece;

        //side 0 = aPiece = enemy, side 1 = dPiece = enemy
        int side = aPiece.getPieceType().equals(Piece.PieceType.GENERIC) ? 0 : 1;

        //Assume isPresent for now
        if (side == 0)
            dPiece = Board.getEnemyPlayer().getPiece(aPiece.getColumn(), aPiece.getRow()).get();
        else
            dPiece = Board.getEnemyPlayer().getPiece(zPiece.getColumn(), zPiece.getRow()).get();

        Animation.playAnimation(dPiece.getColumn(), dPiece.getRow(), dPiece.getPieceType().getSpriteIndex());

        if (aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            pieces[dPiece.getColumn()][dPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(dPiece.getPosition());
            Board.addCapturedPiece(dPiece.getPieceType());
            Board.addLostPiece(aPiece.getPieceType());
        } else if (aPiece.getPieceType().getCombatValue() > dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            Board.addLostPiece(aPiece.getPieceType());
        } else {
            pieces[dPiece.getColumn()][dPiece.getRow()] = aPiece.clone().setPosition(dPiece.getPosition());
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            Board.addCapturedPiece(dPiece.getPieceType());
        }

        Board.setPieces(pieces);

    }


    public static java.util.List<Piece> getMovableTiles(Piece piece) {
        java.util.List<Piece> pieces = new ArrayList<Piece>();
        if (piece != null) {
            int mx = piece.getColumn();
            int my = piece.getRow();
            for (Board.Direction dir : Board.Direction.values()) {
                for (int i = 0; i < piece.getMaxDistance(); ++i) {
                    int dx = dir.equals(Board.Direction.LEFT) || dir.equals(Board.Direction.RIGHT) ? mx + (dir.getOffset() * (i + 1)) : mx;
                    int dy = dir.equals(Board.Direction.UP) || dir.equals(Board.Direction.DOWN) ? my + (dir.getOffset() * (i + 1)) : my;
                    if (dx >= 0 && dy >= 0 && dx < SIZE_X && dy < SIZE_Y) {
                        Piece p = Board.getPiece(dx, dy);

                        if (p.getPieceType().equals(Piece.PieceType.EMPTY) || p.getPieceType().equals(Piece.PieceType.GENERIC))
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
