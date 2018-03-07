package client.screens;

import client.resources.Images;
import game.Board;
import game.Piece;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by Alek on 3/6/2018.
 */
public class GameBoard {

    private static final int TILE_SIZE = 64;

    public static void paintScreen(Graphics g, ImageObserver o) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(Images.getImage("background_0"), 0, 0, o);
        g2d.setColor(Color.WHITE);
        for (int x = 0; x < Board.SIZE_X; ++x) {
            for (int y = 0; y < Board.SIZE_Y; ++y) {
                int xOffset = x * TILE_SIZE;
                int yOffset = y * TILE_SIZE;
                Piece piece = Board.getPiece(x, y);
                if(piece.getPieceType() != Piece.PieceType.EMPTY) {
                    if(piece.getPieceType().equals(Piece.PieceType.GENERIC)) {
                        g.drawImage(Images.getImage("stone"), xOffset, yOffset, o);
                    } else {
                        g.drawImage(Images.getImage("wood"), xOffset, yOffset, o);
                        g.drawImage(Images.getImage(String.valueOf(piece.getPieceType().getSpriteIndex())), xOffset, yOffset, o);
                    }
                }
                g2d.drawRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
            }
        }
    }

}
