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
    public static final String P_INSETUP = "INSETUP";											//Sent to client when pvp starts
    public static final String P_INGAME = "INGAME";												//Sent to client when pvp starts

}