package game;

import client.Global;
import client.resources.Images;
import client.screens.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Alek on 3/2/2018.
 */
public class Board implements Screen {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;
    public static final int TILE_SIZE = 64;

    private static Piece selectedPiece = null;

    static Piece[][] pieces = new Piece[SIZE_X][SIZE_Y];

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

    public static void initialize() {
        for (int x = 0; x < SIZE_X; ++x)
            for (int y = 0; y < SIZE_Y; ++y)
                pieces[x][y] = new Piece(Piece.PieceType.EMPTY);
    }

    public static Piece getPiece(int col, int row) {
        return pieces[col][row];
    }

    public static void setPiece(int col, int row, Piece piece) {
        pieces[col][row] = piece.setPosition(col, row);
    }

    public void attack(Piece aPiece, Piece dPiece) {

        if (aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY);
            pieces[dPiece.getColumn()][dPiece.getRow()] = new Piece(Piece.PieceType.EMPTY);
        } else if (aPiece.getPieceType().getCombatValue() < dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY); //move from old
            pieces[dPiece.getColumn()][dPiece.getRow()] = aPiece; //move to new
        } else {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY); //remove from old
        }
    }

    private java.util.List<Piece> getMovableTiles(Piece piece) {
        java.util.List<Piece> pieces = new ArrayList<Piece>();
        if(piece != null) {
            int mx = piece.getColumn();
            int my = piece.getRow();
            for(Direction dir : Direction.values()) {
                if (dir.equals(Direction.UP) || dir.equals(Direction.DOWN)) {
                    for(int i = 0; i < piece.getMaxDistance(); ++i) {
                        int dx = mx;
                        int dy = my + (dir.getOffset() * (i + 1));
                        if(dy >= 0 && dy < SIZE_Y)
                            pieces.add(getPiece(dx, dy));
                    }
                }
                else if (dir.equals(Direction.LEFT) || dir.equals(Direction.RIGHT)) {
                        for(int i = 0; i < piece.getMaxDistance(); ++i) {
                            int dx = mx + (dir.getOffset() * (i + 1));
                            int dy = my;
                            if(dx >= 0 && dx < SIZE_X)
                                pieces.add(getPiece(dx, dy));
                        }
                }
            }
        }
        pieces.removeAll(pieces.stream().filter(p ->
                !p.getPieceType().equals(Piece.PieceType.EMPTY) &&
                        !p.getPieceType().equals(Piece.PieceType.GENERIC)
        ).collect(Collectors.toList()));
        return pieces;
    }


    public void processEvent(MouseEvent e) {
        Stream.of(pieces).flatMap(Stream::of).forEach(i -> {
            if (i.getBounds().contains(e.getPoint())) {
                if (i.getPieceType().isSelectable()) {
                    if (!i.equals(selectedPiece)) {
                        selectedPiece = i;
                    } else {
                        selectedPiece = null;
                    }
                }
            }
        });
    }

    public void paintScreen(Graphics g, ImageObserver o) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(Images.getImage("background_0"), 0, 0, o);
        java.util.List<Piece> pieces = getMovableTiles(selectedPiece);
        g2d.setColor(Color.WHITE);
        for (int x = 0; x < Board.SIZE_X; ++x) {
            for (int y = 0; y < Board.SIZE_Y; ++y) {
                int xOffset = x * TILE_SIZE;
                int yOffset = y * TILE_SIZE;
                Piece piece = Board.getPiece(x, y);
                if (piece.getPieceType() != Piece.PieceType.EMPTY) {
                    if (piece.getPieceType().equals(Piece.PieceType.GENERIC)) {
                        g.drawImage(Images.getImage("stone"), xOffset, yOffset, o);
                    } else {
                        g.drawImage(Images.getImage("wood"), xOffset, yOffset, o);
                        g.drawImage(Images.getImage(String.valueOf(piece.getPieceType().getSpriteIndex())), xOffset, yOffset, o);
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(piece.getPieceType().getCombatValue()), xOffset + (TILE_SIZE - 10), yOffset + (TILE_SIZE - 10));
                        g.setColor(Color.WHITE);
                        g.drawString(String.valueOf(piece.getPieceType().getCombatValue()), xOffset + (TILE_SIZE - 9), yOffset + (TILE_SIZE - 9));
                    }
                }
                if (piece.equals(selectedPiece)) {
                    g.setColor(Color.GREEN);
                    g.drawRect(xOffset, yOffset, TILE_SIZE - 2, TILE_SIZE - 2);
                    g.drawRect(xOffset + 1, yOffset + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.drawRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
                }
                if(pieces.contains(piece)) {
                    g.setColor(new Color(0, 155, 0, 128));
                    g.fillRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }


}
