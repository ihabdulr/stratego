package client;

import server.Server;

/**
 * Created by Alek on 3/2/2018.
 */
public class Global {

    public static int WIDTH = 1024;
    public static int HEIGHT = 768;
    private static GameState gameState = GameState.MENU;
    private static BoardState boardState = BoardState.MY_TURN;
    private static Network connectedServer;
    
    public enum BoardState {SETUP, MY_TURN, THEIR_TURN, GAME_WON, GAME_LOSS}
    public enum GameState {GAME, MENU, CREDITS}
    
    public static GameState getGameState() {
        return gameState;
    }
    public static void setGameState(GameState state) {
        gameState = state;
    }
    public static BoardState getBoardState() {
        return boardState;
    }
    public static void setBoardState(BoardState state) {
        boardState = state;
    }
    
    public static void setServer(Network serve) {
    	connectedServer = serve;
    }
    
    public static Network getServer() {
    	return connectedServer;
    }

}
