package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


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
            Piece.PieceType.SCOUT,
            Piece.PieceType.BOMB_DEFUSER, Piece.PieceType.BOMB_DEFUSER, Piece.PieceType.BOMB_DEFUSER,
            Piece.PieceType.MINION, Piece.PieceType.MINION,Piece.PieceType.MINION, Piece.PieceType.MINION, Piece.PieceType.MINION, Piece.PieceType.MINION,
            Piece.PieceType.MINOTAUR, Piece.PieceType.MINOTAUR,
            Piece.PieceType.SKULL_PRINCE, Piece.PieceType.SKULL_KING,
            Piece.PieceType.BOMB, Piece.PieceType.BOMB, Piece.PieceType.BOMB, Piece.PieceType.FLAG
    };

    public static java.util.List<Piece.PieceType> getGamePieces() {
        return Arrays.asList(GAME_PIECES);
    }

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
        clear();
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
