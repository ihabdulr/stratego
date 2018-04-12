package game;

import client.Global;
import client.resources.Images;
import client.screens.Screen;
import game.player.AIPlayer;
import game.player.GamePlayer;
import game.player.LocalPlayer;
import game.player.NetworkPlayer;
import server.Packets;

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

    private static BoardButton autoFillButton = new BoardButton((Global.WIDTH / 2) + 32, 40, (Global.WIDTH / 2) - 64, 48, "Auto-Fill");
    private static BoardButton clearButton = new BoardButton((Global.WIDTH / 2) + 32, 136, (Global.WIDTH / 2) - 64, 48, "Clear Board");
    private static BoardButton loadButton = new BoardButton((Global.WIDTH / 2) + 32, 232, (Global.WIDTH / 2) - 64, 48, "Load Setup");
    private static BoardButton saveButton = new BoardButton((Global.WIDTH / 2) + 32, 328, (Global.WIDTH / 2) - 64, 48, "Save Setup");
    private static BoardButton readyButton = new BoardButton((Global.WIDTH / 2) + 32, 424, (Global.WIDTH / 2) - 64, 48, "Ready");
    private BoardButton exitMainButton = new BoardButton(64, 640, (Global.WIDTH / 2) - 128, 48, "Exit to Main Menu");
    private static BoardButton mainMenuButton = new BoardButton((Global.WIDTH / 4) + 32, Global.HEIGHT / 2,
            (Global.WIDTH / 2) - 64, 64, "Main Menu");


    private java.util.List<BoardButton> setupButtons = Arrays.asList(
            autoFillButton, clearButton, readyButton.setEnabled(false),
            loadButton.setEnabled(SaveLoad.stateFileExists()), saveButton.setEnabled(false));

    public static Map<Piece.PieceType, Integer> capturedPieces = new HashMap<>();
    public static Map<Piece.PieceType, Integer> lostPieces = new HashMap<>();

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

    public static GamePlayer getCurrentOpposingPlayer() {
        return Global.getBoardState().equals(Global.BoardState.MY_TURN) ? enemyPlayer : localPlayer;
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

    private static SetupContainer setupContainer = new SetupContainer(15, 64 * SIZE_Y + 48, Global.WIDTH - 31, 160);

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
        //Rocks
        pieces[3][3] = new Piece(Piece.PieceType.BLOCK).setPosition(3, 3);
        pieces[3][4] = new Piece(Piece.PieceType.BLOCK).setPosition(3, 4);
        pieces[4][3] = new Piece(Piece.PieceType.BLOCK).setPosition(4, 3);
        pieces[4][4] = new Piece(Piece.PieceType.BLOCK).setPosition(4, 4);
        setupContainer.initialize();
        autoFillButton.setEnabled(true);
        readyButton.setEnabled(false);
        saveButton.setEnabled(false);
        loadButton.setEnabled(true);
        selectedPiece = null;
        selectedTile = null;
        selectedButton = null;
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

    public static Piece getPiece(int col, int row) {
        return pieces[col][row];
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

    public enum TurnState {VALID, INVALID, NO_MORE, FLAG_CAPTURED}

    public static TurnState move(Piece start, Piece end2) {

        Piece end = getPiece(end2.getColumn(), end2.getRow());

        //simple swap
        if (end.getPieceType().equals(Piece.PieceType.EMPTY)) {
            movePiece(start, end);
            selectedPiece = null;
            return getCurrentPlayer().hasAtLeastOneMovablePiece() ? TurnState.VALID : TurnState.NO_MORE;
        }

        if (getCurrentOpposingPlayer().getPieces().contains(end)) {
            boolean flag = GameLogic.attack(start, end);
            selectedPiece = null;
            if (flag)
                return TurnState.FLAG_CAPTURED;
            return getCurrentPlayer().hasAtLeastOneMovablePiece() ? TurnState.VALID : TurnState.NO_MORE;
        }

        return TurnState.INVALID;
    }


    public void processMousePressedEvent(MouseEvent e) {
        if (!Global.isGameOver()) {
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
                            TurnState result = move(selectedPiece, i);
                            if (result.equals(TurnState.VALID)) {
                                Global.setBoardState(Global.BoardState.THEIR_TURN);
                                ((AIPlayer) enemyPlayer).nextMove();
                            } else if (result.equals(TurnState.NO_MORE)) {
                                Global.setBoardState(Global.BoardState.GAME_LOSS);
                            } else if (result.equals(TurnState.FLAG_CAPTURED)) {
                                if (Global.getBoardState().equals(Global.BoardState.MY_TURN))
                                    Global.setBoardState(Global.BoardState.GAME_WON);
                                else
                                    Global.setBoardState(Global.BoardState.GAME_LOSS);
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
                                //Global.setBoardState(Global.BoardState.GAME_WON); //used for testing
                                Global.setBoardState(Global.BoardState.MY_TURN);
                            } else {
                                //TODO tell the server we're ready to go
                                enemyPlayer = new NetworkPlayer();
                                Global.connectedServer.addCommand(Packets.P_SEND_PIECES + SaveLoad.getPiecesAsString());
                            }

                        } else if (button.equals(clearButton)) {
                            initialize();
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
            else if (exitMainButton.getBounds().contains(e.getPoint()) && mainMenuButton.isEnabled()) {
                Global.setGameState(Global.GameState.MENU);
                Global.setBoardState(Global.BoardState.SETUP);
            }
        } else { //game is over
            if (mainMenuButton.getBounds().contains(e.getPoint()) && mainMenuButton.isEnabled()) {
                Global.setGameState(Global.GameState.MENU);
                Global.setBoardState(Global.BoardState.SETUP);
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
        } else if (Global.isGameOver()) {
            if (mainMenuButton.getBounds().contains(e.getPoint())) {
                mainMenuButton.setHighlighted(true);
            } else {
                mainMenuButton.setHighlighted(false);
            }
        } else if (exitMainButton.getBounds().contains(e.getPoint())) {
            exitMainButton.setHighlighted(true);
        } else {
            exitMainButton.setHighlighted(false);
        }
    }

    public void formatButton(Graphics g, BoardButton button) {
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

    private String formatEnum(String name) {
        return name.charAt(0) + name.toLowerCase().replace("_", " ").substring(1);
    }

    public void paintScreen(Graphics g, ImageObserver o) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(Images.getImage("background_0"), 0, 0, o);
        java.util.List<Piece> pieces = GameLogic.getMovableTiles(selectedPiece);
        g2d.setColor(Color.WHITE);
        for (int x = 0; x < Board.SIZE_X; ++x) {
            for (int y = 0; y < Board.SIZE_Y; ++y) {
                int xOffset = x * TILE_SIZE;
                int yOffset = y * TILE_SIZE;
                Piece piece = Board.getPiece(x, y);
                if (piece.getPieceType() != Piece.PieceType.EMPTY) {
                    if (piece.getPieceType().equals(Piece.PieceType.GENERIC)) {
                        g.drawImage(Images.getImage("stone"), xOffset, yOffset, o);
                    } else if (piece.getPieceType().getCombatValue() > -1) {
                        g.drawImage(Images.getImage("wood"), xOffset, yOffset, o);
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(piece.getPieceType().getCombatValue()), xOffset + (TILE_SIZE - 10), yOffset + (TILE_SIZE - 10));
                        g.setColor(Color.WHITE);
                        g.drawString(String.valueOf(piece.getPieceType().getCombatValue()), xOffset + (TILE_SIZE - 9), yOffset + (TILE_SIZE - 9));
                    }
                    if (piece.getPieceType().getSpriteIndex() > -1)
                        g.drawImage(Images.getImage(String.valueOf(piece.getPieceType().getSpriteIndex())), xOffset, yOffset, o);
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
            g.setColor(redTransparent);
            g.fillRect(Animation.getX() * TILE_SIZE, Animation.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
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
                formatButton(g, button);
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

            if (selectedPiece != null) {
                g.setColor(Color.YELLOW);
                g.setFont(titleFont);
                g.drawString("Selected Piece: " + formatEnum(selectedPiece.getPieceType().name()), 15, 550);
            }
            g.drawString("Turn: " + (Global.getBoardState().equals(Global.BoardState.MY_TURN) ? "My Turn" : "Enemy turn"), 15, 575);

            if (Global.isGameOver()) {
                g.setColor(blackTransparent);
                g.fillRect(0, 0, Global.WIDTH, Global.HEIGHT);
                g.fillRect(Global.WIDTH / 4, Global.HEIGHT / 4, Global.WIDTH / 2, Global.HEIGHT / 2);
                g.setColor(Color.YELLOW);
                g.drawString(Global.getBoardState().name().replace("_", " ") + "!",
                        465, 250);
                formatButton(g, mainMenuButton);
            } else {
                formatButton(g, exitMainButton);
            }

        }
    }
}
