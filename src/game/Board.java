package game;

import client.Global;
import client.resources.Images;
import client.screens.Screen;
import game.player.AIPlayer;
import game.player.GamePlayer;
import game.player.LocalPlayer;
import game.player.NetworkPlayer;

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
            loadButton.setEnabled(SaveLoad.stateFileExists()), saveButton.setEnabled(false));

    private static Map<Piece.PieceType, Integer> capturedPieces = new HashMap<>();
    private static Map<Piece.PieceType, Integer> lostPieces = new HashMap<>();

    private static GamePlayer enemyPlayer;
    private static LocalPlayer localPlayer;


    public static GamePlayer getEnemyPlayer() {
        return enemyPlayer;
    }

    public static GamePlayer getLocalPlayer() {
        return localPlayer;
    }

    public static GamePlayer getCurrentPlayer() {
        return Global.getBoardState().equals(Global.BoardState.MY_TURN) ? localPlayer : enemyPlayer;
    }

    public static void setPieces(Piece[][] newPieces) {
        pieces = newPieces;
    }

    public static Piece[][] getPieces() {
        return pieces;
    }

    public static void addPieces(java.util.List<Piece> p) {
        p.forEach(i -> {
            pieces[i.getColumn()][i.getRow()] = i;
        });
    }

    public static java.util.List<Piece> getBoardPieceObjects(java.util.List<Piece> alias) {
        java.util.List<Piece> returnPieces = new ArrayList<Piece>();
        for (Piece p : alias) {
            Optional<Piece> piece = Stream.of(pieces).flatMap(Stream::of).filter(i -> i.getPieceType().equals(p.getPieceType()) &&
                    i.getPosition().equals(p.getPosition())).findFirst();
            if (piece.isPresent()) {
                returnPieces.add(piece.get());
            } else {
                System.out.println("FAILED TO CONVERT!");
            }
        }
        return returnPieces;
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
        localPlayer = new LocalPlayer();
        for (int x = 0; x < SIZE_X; ++x)
            for (int y = 0; y < SIZE_Y; ++y)
                pieces[x][y] = new Piece(Piece.PieceType.EMPTY).setPosition(x, y);
    }

    private void autoFillBoard() {
        initialize();
        setupContainer.clear();
        java.util.List<Piece.PieceType> pieces2 = SetupContainer.getGamePieces();
        Collections.shuffle(pieces2);
        int flatMap = pieces2.size();
        for (int x = 0; x < SIZE_X; ++x) {
            for (int y = 5; y < SIZE_Y; ++y) {
                pieces[x][y] = new Piece(pieces2.get(flatMap - 1)).setPosition(x, y);
                --flatMap;
            }
        }
        readyButton.setEnabled(true);
    }


    //Returns a piece in the context of Local Playerflatmap
    public static Piece getPiece(int col, int row) {
        //  if (Global.getBoardState().equals(Global.BoardState.SETUP))
        return pieces[col][row];
        //   return GameLogic.getPiece(new Point(col, row), Board.getLocalPlayer(), Board.getEnemyPlayer());
    }


    public static void setPiece(int col, int row, Piece piece) {
        pieces[col][row] = piece.setPosition(col, row);
    }

    //Moves a piece, placing an empty piece in the old location
    public static void movePiece(Piece piece, Piece dest) {
        if (Board.getCurrentPlayer().equals(enemyPlayer))
            Board.getEnemyPlayer().movePiece(piece, dest.getPosition());
        pieces[dest.getColumn()][dest.getRow()] = pieces[piece.getColumn()][piece.getRow()].clone().setPosition(dest.getPosition());
        pieces[piece.getColumn()][piece.getRow()] = dest.clone().setPosition(piece.getPosition());

    }

    public static void addCapturedPiece(Piece.PieceType pieceType) {
        System.out.println("Captured " + pieceType);
        if (!capturedPieces.containsKey(pieceType)) {
            capturedPieces.put(pieceType, 1);
        } else {
            capturedPieces.replace(pieceType, capturedPieces.get(pieceType) + 1);
        }
    }

    public static void addLostPiece(Piece.PieceType pieceType) {
        System.out.println("Lost " + pieceType);
        if (!lostPieces.containsKey(pieceType)) {
            lostPieces.put(pieceType, 1);
        } else {
            lostPieces.replace(pieceType, lostPieces.get(pieceType) + 1);
        }
    }

    public static boolean move(Piece start, Piece end) {

        //DO NOT FOR THE LOVE OF GOD USE .contains, IT WILL RETURN FALSE
        //BECAUSE .contains CHECKS FOR THE OBJECT ADDRESS
        //Alternatively use the getBoardPieceObjects() method
        if (GameLogic.getMovableTiles(start).stream().noneMatch(p ->
                p.getPosition().equals(end.getPosition()) && p.getPieceType().equals(end.getPieceType())))
            return false;

        //simple swap
        if (end.getPieceType().equals(Piece.PieceType.EMPTY)) {
            movePiece(start, end);
            selectedPiece = null;
            return true;
        }

        if (end.getPieceType().equals(Piece.PieceType.GENERIC)) {
            GameLogic.attack(start, end.clone(enemyPlayer.getPiece(end.getPosition()).get().getPieceType()));
            selectedPiece = null;
            return true;
        }

        return false;
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
                        if (move(selectedPiece, i)) {
                            Global.setBoardState(Global.BoardState.THEIR_TURN);
                            ((AIPlayer) enemyPlayer).nextMove();
                        }
                    }
                } else if (Global.getBoardState().equals(Global.BoardState.SETUP)) {
                    if (selectedTile != null) {
                        if (i.getPieceType().equals(Piece.PieceType.EMPTY)) {
                            pieces[i.getColumn()][i.getRow()] = new Piece(selectedTile.getType()).setPosition(i.getColumn(), i.getRow());
                            setupContainer.removeTile(selectedTile);
                            selectedTile = null;
                            if (setupContainer.getSetupTiles().isEmpty()) {
                                readyButton.setEnabled(true);
                                saveButton.setEnabled(true);
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
                    if (button.equals(readyButton)) {
                        if (!Global.isNetworkGame()) {
                            enemyPlayer = new AIPlayer();
                            addPieces(enemyPlayer.getSanitizedPieces());
                        } else {
                            //TODO tell the server we're ready to go
                            enemyPlayer = new NetworkPlayer();
                        }
                        Global.setBoardState(Global.BoardState.MY_TURN);
                    } else if (button.equals(clearButton)) {
                        initialize();
                        setupContainer.initialize();
                        autoFillButton.setEnabled(true);
                        readyButton.setEnabled(false);
                        saveButton.setEnabled(false);
                        loadButton.setEnabled(true);
                    } else if (button.equals(autoFillButton)) {
                        autoFillBoard();
                        autoFillButton.setEnabled(false);
                        readyButton.setEnabled(true);
                        saveButton.setEnabled(true);
                    } else if (button.equals(saveButton)) {
                        SaveLoad.saveSetup();
                        loadButton.setEnabled(false);
                    } else if (button.equals(loadButton)) {
                        SaveLoad.loadSetup();
                        loadButton.setEnabled(false);
                        readyButton.setEnabled(true);
                        setupContainer.clear();
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
        java.util.List<Piece> pieces = Board.getBoardPieceObjects(GameLogic.getMovableTiles(selectedPiece));
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

        if (Animation.shouldAnimate()) {
            //Piece p = Animation.getAnimationPiece();
            g.drawImage(Images.getImage(String.valueOf(Animation.getAnimationIndex())),
                    Animation.getX() * TILE_SIZE, Animation.getY() * TILE_SIZE, o);
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
        } else {
            g.setColor(Color.WHITE);
            g.drawLine(Global.WIDTH - (Global.WIDTH / 4), 0, Global.WIDTH - (Global.WIDTH / 4), Global.HEIGHT);
            g.drawLine(Global.WIDTH - (Global.WIDTH / 2), 0, Global.WIDTH - (Global.WIDTH / 2), Global.HEIGHT);
            g.setColor(Color.YELLOW);
            g.setFont(titleFont);
            g.drawString("Captured Pieces", Global.WIDTH - (Global.WIDTH / 2) + 60, 35);
            int i = 0;
            for (java.util.Map.Entry<game.Piece.PieceType, Integer> p : capturedPieces.entrySet()) {
                g.drawImage(Images.getImage(String.valueOf(p.getKey().getSpriteIndex())),
                        Global.WIDTH - (Global.WIDTH / 2) + 45, 60 + (i * TILE_SIZE + 16), o);
                g.drawString("x" + p.getValue(), Global.WIDTH - (Global.WIDTH / 2) + 45 + TILE_SIZE, 60 + (i * TILE_SIZE + 16) + 32);
                ++i;
            }
            i = 0;
            g.drawString("Lost Pieces", Global.WIDTH - (Global.WIDTH / 4) + 70, 35);
            for (java.util.Map.Entry<game.Piece.PieceType, Integer> p : lostPieces.entrySet()) {
                g.drawImage(Images.getImage(String.valueOf(p.getKey().getSpriteIndex())),
                        Global.WIDTH - (Global.WIDTH / 4) + 45, 60 + (i * TILE_SIZE + 16), o);
                g.drawString("x" + p.getValue(), Global.WIDTH - (Global.WIDTH / 4) + 45 + TILE_SIZE, 60 + (i * TILE_SIZE + 16) + 32);
                ++i;
            }

        }
    }
}
