package game;

import client.Global;
import client.resources.Images;
import client.screens.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Alek on 3/2/2018.
 */
public class Board implements Screen {

    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;
    public static final int TILE_SIZE = 64;

    private static Piece selectedPiece = null;
    private static SetupContainer.SetupTile selectedTile = null;
    private static BoardButton selectedButton = null;

    private Font titleFont = new Font("Sans-Serif", Font.BOLD, 16);
    private Color blackTransparent = new Color(0, 0, 0, 128);
    private Color whiteTransparent = new Color(192, 192, 192, 128);
    private Color greenTransparent = new Color(0, 155, 0, 128);
    private Color redTransparent = new Color(155, 0, 0, 128);
    private Color yellowTransparent = new Color(155, 155, 0, 128);
    private Color yellowTransparent2 = new Color(155, 155, 0, 64);

    private BoardButton autoFillButton = new BoardButton((Global.WIDTH / 2) + 32, 40, (Global.WIDTH / 2) - 64, 48, "Auto-Fill");
    private BoardButton clearButton = new BoardButton((Global.WIDTH / 2) + 32, 136, (Global.WIDTH / 2) - 64, 48, "Clear Board");
    private BoardButton loadButton = new BoardButton((Global.WIDTH / 2) + 32, 232, (Global.WIDTH / 2) - 64, 48, "Load Setup");
    private BoardButton saveButton = new BoardButton((Global.WIDTH / 2) + 32, 328, (Global.WIDTH / 2) - 64, 48, "Save Setup");
    private BoardButton readyButton = new BoardButton((Global.WIDTH / 2) + 32, 424, (Global.WIDTH / 2) - 64, 48, "Ready");

    private java.util.List<BoardButton> setupButtons = Arrays.asList(
            autoFillButton, clearButton, readyButton.setEnabled(false),
            loadButton, saveButton);

    private static Map<Piece.PieceType, Integer> capturedPieces = new HashMap<>();
    private static Map<Piece.PieceType, Integer> lostPieces = new HashMap<>();



    public static void setPieces(Piece[][] newPieces) {
        pieces = newPieces;
    }

    public static Piece[][] getPieces() {
        return pieces;
    }


    private SetupContainer setupContainer = new SetupContainer(15, 64 * SIZE_Y + 48, Global.WIDTH - 31, 160);

    static Piece[][] pieces = new Piece[SIZE_X][SIZE_Y];

    public enum Direction {
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
                pieces[x][y] = new Piece(Piece.PieceType.EMPTY).setPosition(x, y);
    }

    private void autoFillBoard() {
        initialize();
        setupContainer.clear();
        int flatIndex = 0;
        for(int x = 0; x < SIZE_X; ++x) {
            for(int y = 5; y < SIZE_Y; ++y) {
                pieces[x][y] = new Piece(SetupContainer.GAME_PIECES[flatIndex]).setPosition(x, y);
                ++flatIndex;
            }
        }
        readyButton.setEnabled(true);
    }

    public static Piece getPiece(int col, int row) {
        return pieces[col][row];
    }

    public static void setPiece(int col, int row, Piece piece) {
        pieces[col][row] = piece.setPosition(col, row);
    }

    //Moves a piece, placing an empty piece in the old location
    public void movePiece(Piece piece, Piece dest) {
        pieces[dest.getColumn()][dest.getRow()] = piece.clone().setPosition(dest.getPosition());
        pieces[piece.getColumn()][piece.getRow()] = dest.clone().setPosition(piece.getPosition());
    }

    private void addCapturedPiece(Piece.PieceType pieceType) {
        System.out.println("Captured " + pieceType);
        if (!capturedPieces.containsKey(pieceType)) {
            capturedPieces.put(pieceType, 1);
        } else {
            capturedPieces.replace(pieceType, capturedPieces.get(pieceType) + 1);
        }
    }

    private void addLostPiece(Piece.PieceType pieceType) {
        System.out.println("Lost " + pieceType);
        if (!lostPieces.containsKey(pieceType)) {
            lostPieces.put(pieceType, 1);
        } else {
            lostPieces.replace(pieceType, lostPieces.get(pieceType) + 1);
        }
    }

    public void attack(Piece aPiece, Piece dPiece) {
        //TODO play animation here
        if (aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            pieces[dPiece.getColumn()][dPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(dPiece.getPosition());
            addCapturedPiece(dPiece.getPieceType());
            addLostPiece(aPiece.getPieceType());
        } else if (aPiece.getPieceType().getCombatValue() > dPiece.getPieceType().getCombatValue()) {
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            addLostPiece(aPiece.getPieceType());
        } else {
            pieces[dPiece.getColumn()][dPiece.getRow()] = aPiece.clone().setPosition(dPiece.getPosition());
            pieces[aPiece.getColumn()][aPiece.getRow()] = new Piece(Piece.PieceType.EMPTY).setPosition(aPiece.getPosition());
            addCapturedPiece(dPiece.getPieceType());
        }
    }

    //Requests from server or game logic to reveal generic piece
    public Piece.PieceType requestPiece(Piece p) {
        //For now, just return mid-tier piece
        return Piece.PieceType.SERGEANT;
    }

    public boolean move(Piece start, Piece end) {
        java.util.List<Piece> available = getMovableTiles(start);
        if (!available.contains(end))
            return false;

        //simple swap
        if (end.getPieceType().equals(Piece.PieceType.EMPTY)) {
            movePiece(start, end);
            selectedPiece = null;
            return true;
        }

        if (end.getPieceType().equals(Piece.PieceType.GENERIC)) {
            attack(start, end.clone(requestPiece(end)));
            selectedPiece = null;
        }

        return false;
    }


    private java.util.List<Piece> getMovableTiles(Piece piece) {
        java.util.List<Piece> pieces = new ArrayList<Piece>();
        if (piece != null) {
            int mx = piece.getColumn();
            int my = piece.getRow();
            for (Direction dir : Direction.values()) {
                for (int i = 0; i < piece.getMaxDistance(); ++i) {
                    int dx = dir.equals(Direction.LEFT) || dir.equals(Direction.RIGHT) ? mx + (dir.getOffset() * (i + 1)) : mx;
                    int dy = dir.equals(Direction.UP) || dir.equals(Direction.DOWN) ? my + (dir.getOffset() * (i + 1)) : my;
                    if (dx >= 0 && dy >= 0 && dx < SIZE_X && dy < SIZE_Y) {
                        Piece p = getPiece(dx, dy);

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


    public void processMousePressedEvent(MouseEvent e) {
        Stream.of(pieces).flatMap(Stream::of).forEach(i -> {
            if (i.getBounds().contains(e.getPoint())) {
                if (Global.getBoardState().equals(Global.BoardState.MY_TURN)) {
                    if (i.getPieceType().isSelectable()) {
                        if (selectedPiece == null) {
                            selectedPiece = i;
                        } else if (selectedPiece.equals(i)) {
                            selectedPiece = null;
                        }
                    } else if (selectedPiece != null) {
                        e.consume();
                        move(selectedPiece, i);
                    }
                } else if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
                    if (selectedTile != null) {
                        if (i.getPieceType().equals(Piece.PieceType.EMPTY)) {
                            pieces[i.getColumn()][i.getRow()] = new Piece(selectedTile.getType()).setPosition(i.getColumn(), i.getRow());
                            setupContainer.removeTile(selectedTile);
                            selectedTile = null;
                            if (setupContainer.getSetupTiles().isEmpty()) {
                                //TODO tell the server that we're done setting up
                                setupButtons.get(setupButtons.indexOf(readyButton)).setEnabled(true);
                            }
                        }
                    }
                }
            }
        });

        if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
            for (SetupContainer.SetupTile tile : setupContainer.getSetupTiles()) {
                if (tile.getBounds().contains(e.getPoint())) {
                    if (selectedTile == null) {
                        selectedTile = tile;
                    } else if (selectedTile.equals(tile)) {
                        selectedTile = null;
                    }
                }
            }
            for (BoardButton button : setupButtons) {
                if (button.getBounds().contains(e.getPoint()) && button.isEnabled()) {
                    if(button.equals(readyButton)) {
                        Global.setBoardState(Global.BoardState.MY_TURN);
                    } else if(button.equals(clearButton)) {
                        initialize();
                        setupContainer.initialize();
                        autoFillButton.setEnabled(true);
                        readyButton.setEnabled(false);
                    } else if(button.equals(autoFillButton)) {
                        autoFillBoard();
                        autoFillButton.setEnabled(false);
                        readyButton.setEnabled(true);
                    }
                }
            }
        }

    }

    public void processMouseMovedEvent(MouseEvent e) {
        if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
            for (BoardButton button : setupButtons) {
                if (button.getBounds().contains(e.getPoint())) {
                    button.setHighlighted(true);
                } else {
                    button.setHighlighted(false);
                }
            }
        }
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
                } else if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
                    if (y > 4) {
                        g.setColor(yellowTransparent2);
                        g.fillRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
                    }
                }
                if (piece.equals(selectedPiece)) {
                    if (piece.getPieceType().equals(Piece.PieceType.EMPTY))
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(Color.RED);
                    g.drawRect(xOffset, yOffset, TILE_SIZE - 2, TILE_SIZE - 2);
                    g.drawRect(xOffset + 1, yOffset + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.drawRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
                }
                if (pieces.contains(piece)) {
                    if (piece.getPieceType().equals(Piece.PieceType.EMPTY))
                        g.setColor(greenTransparent);
                    else
                        g.setColor(redTransparent);
                    g.fillRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        g.setColor(blackTransparent);
        g.fillRect(0, 64 * SIZE_Y + 2, Global.WIDTH / 2 + 1, 260);
        g.fillRect(Global.WIDTH / 2 + 1, 0, Global.WIDTH / 2, Global.HEIGHT);

        if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
            g.setColor(Color.YELLOW);
            g.setFont(titleFont);
            g.drawString("Place your pieces on the board", 15, 64 * SIZE_Y + 32);
            for (SetupContainer.SetupTile tile : setupContainer.getSetupTiles()) {
                g.setColor(Color.YELLOW);
                g.drawRect(tile.x, tile.y, tile.width, tile.height);
                if (tile.equals(selectedTile)) {
                    g.setColor(yellowTransparent2);
                    g.fillRect(tile.x + 1, tile.y + 1, tile.width - 1, tile.height - 1);
                }
                g.drawImage(Images.getImage(String.valueOf(tile.getType().getSpriteIndex())),
                        tile.x + setupContainer.getTileXOffset(), tile.y + setupContainer.getTileYOffset(), o);
            }
            for (BoardButton button : setupButtons) {
                if (button.isHighlighted())
                    g.setColor(whiteTransparent);
                else
                    g.setColor(blackTransparent);
                g.fillRect(button.x, button.y, button.width, button.height);
                button.setFontMetrics(g.getFontMetrics(titleFont));
                if (button.isEnabled())
                    g.setColor(Color.YELLOW);
                else
                    g.setColor(Color.RED);
                g.drawString(button.getString(), button.getStringX(), button.getStringY());
            }

        }
    }
}
