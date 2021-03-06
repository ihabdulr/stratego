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
        SKULL_KING(1, 1, true, false), SKULL_PRINCE(2, 2, true, false), MINOTAUR(3, 3, true, false), MINION(4, 4, true, false),
        BOMB_DEFUSER(5, 5, true, false), SCOUT(6, 6, true, false), EMPTY(-1, -1, false, false),
        GENERIC(-1, -1, false, false), BLOCK(-1, 12, false, false),
        BOMB(-1, 10, false, true), FLAG(-1, 11, false, true);

        int combatValue, spriteIndex;
        boolean selectable, special;

        PieceType(int combatValue, int spriteIndex, boolean selectable, boolean special) {
            this.combatValue = combatValue;
            this.spriteIndex = spriteIndex;
            this.selectable = selectable;
            this.special = special;
        }

        public boolean isPieceSpecial() {
            return special;
        }

        public int getCombatValue() {
            return combatValue;
        }

        public int getSpriteIndex () {
            return spriteIndex;
        }

        public boolean isSelectable() {
            return selectable;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            return ((Piece) obj).col == col && ((Piece) obj).row == row;
        }
        return false;
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

    public Piece setPosition(Point point) {
        this.col = point.x;
        this.row = point.y;
        return this;
    }

    public Piece clone() {
        return new Piece(pieceType).setPosition(this.getPosition());
    }

    //used to securely clone a piece without exposing
    public Piece clone(PieceType piece) {
        return new Piece(piece).setPosition(this.getPosition());
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

    public Point getPosition() {
        return new Point(col, row);
    }

    public int getMaxDistance() {
        return pieceType.equals(PieceType.SCOUT) ? 10 : 1;
    }

   public Piece setPieceType(PieceType type) {
        this.pieceType = type;
       return this;
   }


}
