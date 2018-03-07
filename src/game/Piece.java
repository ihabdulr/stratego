package game;

/**
 * Created by Alek on 3/2/2018.
 */
public class Piece {

    //Empty is used for empty space
    //Generic is used for an enemy piece that we cant see
    //Block is used for part of the map that we cant move to
    public enum PieceType {
        KING(1, 99), QUEEN(2, 99), SERGEANT(3, 99), PRIVATE(4, 99), SCOUT(5, 0), EMPTY(-1, 99), GENERIC(-1, 99), BLOCK(1, 99), BOMB(-1, 99);

        int combatValue, spriteIndex;

        PieceType(int combatValue, int spriteIndex) {
            this.combatValue = combatValue;
            this.spriteIndex = spriteIndex;
        }

        public int getCombatValue() {
            return combatValue;
        }

        public int getSpriteIndex () {
            return spriteIndex;
        }
    }

    private PieceType pieceType = PieceType.EMPTY;
    private int col, row;

    public Piece setPosition(int col, int row) {
        this.col = col;
        this.row = row;
        return this;
    }

    public void updateImage(String location) {

    }

    public Piece(PieceType pieceType) {
        this.pieceType = pieceType;
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
