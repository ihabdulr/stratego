package client;

/**
 * Created by Alek on 3/2/2018.
 */
public class Global {

    private static GameState gameState = GameState.MENU;
    public enum GameState {GAME, MENU, CREDITS}
    public static GameState getGameState() {
        return gameState;
    }
    public static void setGameState(GameState state) {
        gameState = state;
    }

}
