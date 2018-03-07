package game;

import java.awt.*;

/**
 * Created by Alek on 3/2/2018.
 */
public class Piece {

    //Empty is used for empty space
    //Generic is used for an enemy piece that we cant see
    //Block is used for part of the map that we cant move to
    public enum PieceType {
        KING(1, 1), QUEEN(2, 6), SERGEANT(7, 7), PRIVATE(8, 8), SCOUT(9, 9), EMPTY(-1, 99), GENERIC(-1, 99), BLOCK(1, 99), BOMB(-1, 99);

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

    public Rectangle getBounds(){
        return new Rectangle(col * Board.TILE_SIZE, row * Board.TILE_SIZE, Board.TILE_SIZE, Board.TILE_SIZE);
    }

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
