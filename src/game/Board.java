package game;

/**
 * Created by Alek on 3/2/2018.
 */
public class Board {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;

    Piece[][] pieces = new Piece[SIZE_X][SIZE_Y];

    public static enum Direction {
        UP(-1), DOWN(1), LEFT(-1), RIGHT(1);
        int offset;

        Direction(int offset) {
            this.offset = offset;
        }

        int getOffset() {
            return offset;
        }
    }

    public Piece getPiece(int col, int row) {
        return pieces[col][row];
    }

    public void attack(Piece aPiece, Piece dPiece) {

        if(aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY, aPiece.getColumn(), aPiece.getRow());
            pieces[dPiece.getColumn()][dPiece.getRow()] = new Piece(Piece.PieceType.EMPTY, dPiece.getColumn(), dPiece.getRow());
        } else if(aPiece.getPieceType().getCombatValue() < dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY, aPiece.getColumn(), aPiece.getRow()); //move from old
            pieces[dPiece.getColumn()][dPiece.getRow()] =  aPiece; //move to new
        } else {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY, aPiece.getColumn(), aPiece.getRow()); //remove from old
        }
    }


    public boolean canMove(Piece piece, Direction dir) {
        int dx = piece.getColumn();
        int dy = piece.getRow();
        if(dir.equals(Direction.UP) || dir.equals(Direction.DOWN))
            dy = dy + dir.getOffset();
        else if(dir.equals(Direction.LEFT) || dir.equals(Direction.RIGHT))
            dx = dx + dir.getOffset();

        int distance = (int)Math.sqrt(Math.pow(dx - piece.getColumn(), 2) + Math.pow(dy - piece.getRow(), 2));

        if(distance == 0 || distance > piece.getMaxDistance())
            return false;

        if(dx < 0 || dx > SIZE_X - 1 || dy < 0 || dy > SIZE_Y - 1)
            return false;

        Piece.PieceType destType = getPiece(dx, dy).getPieceType();

        return !destType.equals(Piece.PieceType.BLOCK) && (destType.equals(Piece.PieceType.EMPTY) || destType.equals(Piece.PieceType.GENERIC));

    }


}
