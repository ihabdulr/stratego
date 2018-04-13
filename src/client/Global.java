package client;

import client.screens.MainMenu;
import game.Board;

/**
 * Created by Alek on 3/2/2018.
 */
public class Global {

    public static int WIDTH = 1024;
    public static int HEIGHT = 768;
    private static GameState gameState = GameState.MENU;
    private static BoardState boardState = BoardState.MY_TURN;
    public static Network connectedServer;
    
    public enum BoardState {SETUP, MY_TURN, THEIR_TURN, GAME_WON, GAME_LOSS}
    public enum GameState {GAME, MENU, CREDITS}
    
    public static GameState getGameState() {
        return gameState;
    }
    public static void setGameState(GameState state) {
        if(state.equals(GameState.GAME)) {
            boardState = BoardState.SETUP;
            Board.initialize();
            Board.capturedPieces.clear();
            Board.lostPieces.clear();
        } else if(state.equals(GameState.MENU)) {
            MainMenu.buttonHovered = null;
            MainMenu.buttonPressed = null;
        }
        gameState = state;
    }
    public static BoardState getBoardState() {
        return boardState;
    }
    public static void setBoardState(BoardState state) {
        boardState = state;
    }

    public static boolean isGameOver() {
        return boardState.equals(BoardState.GAME_LOSS) || boardState.equals(BoardState.GAME_WON);
    }
    
    public static void setServer(Network serve) {
    	connectedServer = serve;
    }
    
    public static Network getServer() {
    	return connectedServer;
    }

    private static boolean networkGame = false;

    public static void setGameType(boolean network) {
        networkGame = network;
    }

    public static boolean isNetworkGame() {
        return networkGame;
    }

}
