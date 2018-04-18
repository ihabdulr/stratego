package server;

/**
 * 
 * @author Aaron Roberts
 *
 */
public final class Packets {

    private Packets() {
    }

    public static final String P_PING = "PING";													//Request for a PONG
    public static final String P_REQUEST_ID = "REQUESTID";										//Request it's own client ID
    public static final String P_REQUEST_ADD = "REQUESTADDRESS";								//Request server's address
    public static final String P_DESTROY = "DESTROY";                    						//Destroy this connection
    public static final String P_QUEUE_PLAYER = "QUEUEPLAYER";									//Adds a player to connection queue
    public static final String P_REMOVE_FROM_QUEUE = "REMOVEPLAYER";							//Removes a player from connection queue
    public static final String P_SEND_PIECES = "SENDPIECES";									//Sends pieces to server as string
    public static final String P_SEPERATOR = ".";												//Separator	
    public static final String P_REQUEST_PIECE = "REQUESTPIECE";								//Requests a piece
    public static final String P_GET_PIECES = "GETPIECES";										//Requests a piece
    public static final String P_INSETUP = "INSETUP";											//Sent to client when pvp starts
    public static final String P_INGAME = "INGAME";												//Sent to client when pvp starts
    public static final String P_STATUS_INSETUP = "STATUS_SETUP";								//Asks the server to set player status
    public static final String P_STATUS_READY = "STATUS_READY";									//Asks the server to set player status
    public static final String P_STATUS_INGAME = "STATUS_GAME";									//Asks the server to set player status
    public static final String P_GIVING_PIECES = "GIVINGPIECES";								//Sends all enemy pieces to client
    public static final String P_YOURTURN = "YOURTURN";											//Sends all enemy pieces to client
    public static final String P_THEIRTURN = "THEIRTURN";										//Sends all enemy pieces to client
    public static final String P_SWITCH_TURNS = "SWITCHTURNS";									//Sends all enemy pieces to client

    public static final String P_UPDATE_ENEMY = "UPDATE_ENEMY";									//Sends enemy pieces 
    public static final String P_UPDATE_LOCAL = "UPDATE_LOCAL";									//Sends local
    public static final String P_SEND_BOARD   = "SEND_BOARD";									//sends entire board

}