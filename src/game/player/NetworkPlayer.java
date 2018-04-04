package game.player;

import game.Piece;
import game.SaveLoad;
import server.Packets;

import java.util.Optional;

import client.Global;

/**
 * Created by Alek on 3/23/2018.
 */
public class NetworkPlayer extends GamePlayer {

    @Override
    public Optional<Piece> getPiece(int x, int y) {
   
        //TODO write your code here
        //serverRequest =
        Piece serverRequest1 = null;
    	if (Global.connectedServer.isConnected()) {
    		String xx = Integer.toString(x);
    		String yy = Integer.toString(y);
    		Global.connectedServer.addCommand(Packets.P_REQUEST_PIECE + Packets.P_SEPERATOR + xx + Packets.P_SEPERATOR + yy);
    		serverRequest1 =  SaveLoad.convertPiece(Global.connectedServer.getAnswer());
    	}
        return serverRequest1 == null ? Optional.empty() : Optional.of(serverRequest1);

    }


}
