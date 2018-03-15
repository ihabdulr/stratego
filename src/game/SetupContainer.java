package game;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Alek on 3/14/2018.
 */

//Represents the container used for setting up a game in the board class
public class SetupContainer extends Rectangle {


    public class SetupTile extends Rectangle {
        private Piece.PieceType type;

        public SetupTile(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public SetupTile setType(Piece.PieceType pieceType) {
            type = pieceType;
            return this;
        }

        public Piece.PieceType getType() {
            return type;
        }

    }

    private int trueWidth, trueHeight, tileWidth, tileHeight;
    private int tileXOffset, tileYOffset;
    private java.util.List<SetupTile> setupTiles = new ArrayList<>();
    public final static Piece.PieceType[] GAME_PIECES = {
            Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT,
            Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT, Piece.PieceType.SCOUT,
            Piece.PieceType.PRIVATE, Piece.PieceType.PRIVATE, Piece.PieceType.PRIVATE, Piece.PieceType.PRIVATE, Piece.PieceType.PRIVATE, Piece.PieceType.PRIVATE,
            Piece.PieceType.SERGEANT, Piece.PieceType.SERGEANT, Piece.PieceType.SERGEANT, Piece.PieceType.SERGEANT, Piece.PieceType.QUEEN, Piece.PieceType.KING
    };

    public SetupContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        trueWidth = super.width;
        trueHeight = super.height;
        tileWidth = trueWidth / 12;
        tileHeight = trueHeight / 2;
        tileXOffset = ((tileWidth - 64) / 2);
        tileYOffset = ((tileHeight - 64) / 2);
        initialize();
    }

    public void clear() {
        setupTiles.clear();
    }

    public void initialize() {
        for (int i = 0; i < 24; ++i) {
            int ax = (i < 12 ? i * tileWidth : (i - 12) * tileWidth) + x;
            int ay = (i < 12 ? 0 : tileHeight) + y;
            setupTiles.add(new SetupTile(ax, ay, tileWidth, tileHeight).setType(GAME_PIECES[i]));
        }
    }

    public void removeTile(SetupTile tile) {
        setupTiles.remove(tile);
    }

    public java.util.List<SetupTile> getSetupTiles() {
        return setupTiles;
    }

    public int getTileXOffset() {
        return tileXOffset;
    }

    public int getTileYOffset() {
        return tileYOffset;
    }


}
