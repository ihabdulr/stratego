package game;

/**
 * Created by Alek on 3/2/2018.
 */
public class Piece {

    //Empty is used for empty space
    //Generic is used for an enemy piece that we cant see
    //Block is used for part of the map that we cant move to
    static enum PieceType {
        KING(1), QUEEN(2), SERGEANT(3), PRIVATE(4), SCOUT(5), EMPTY(-1), GENERIC(-1), BLOCK(1), BOMB(-1);

        int combatValue;

        PieceType(int combatValue) {
            this.combatValue = combatValue;
        }

        int getCombatValue() {
            return combatValue;
        }
    }

    private PieceType pieceType;
    private int clientId; //? identifier for the client?
    private int col, row;

    public Piece(PieceType pieceType, int col, int row) {
        this.pieceType = pieceType;
        this.col = col;
        this.row = row;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getColumn() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getMaxDistance() {
        return pieceType.equals(PieceType.SCOUT) ? 10 : 1;
    }


}
