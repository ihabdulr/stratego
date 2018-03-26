package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveLoad {
	
	
	public static void saveSetup() {
		String playerSetup="";
		for(int x=0; x<Board.getLocalPlayer().getPieces().size();x++) {
			if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.KING) {
				playerSetup+="KING:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.QUEEN) {
				playerSetup+="QUEEN:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.SERGEANT) {
				playerSetup+="SERGEANT:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.SCOUT) {
				playerSetup+="SCOUT:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.PRIVATE) {
				playerSetup+="PRIVATE:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.BOMB) {
				playerSetup+="BOMB:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
			else if(Board.getLocalPlayer().getPieces().get(x).getPieceType()==Piece.PieceType.EMPTY) {
				playerSetup+="EMPTY:";
				playerSetup+=Board.getLocalPlayer().getPieces().get(x).getColumn()+":"
						+Board.getLocalPlayer().getPieces().get(x).getRow()+"-";
				
			}
		}
		
		playerSetup+=" ";
		try {
			BufferedWriter text=new BufferedWriter(new FileWriter(new File("PlayerSetup")));
			text.write(playerSetup);
			text.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	
	public static void loadSetup() {
		String playerSetup="";
		BufferedReader textReader=null;
		try {
			textReader=new BufferedReader(new FileReader(new File("PlayerSetup")));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			playerSetup= textReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int strIndex=0;
		//Board.setPiece(4, 4, new Piece(Piece.PieceType.KING));
		for(int x=0; x<Board.getLocalPlayer().getPieces().size();x++) {
			
			//Board.getLocalPlayer().getPieces().size()
			if(playerSetup.substring(strIndex,strIndex+4).equals("KING")) {
				strIndex+=5;
				
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3))  
						    ,new Piece(Piece.PieceType.KING));
				
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex,strIndex+5).equals("QUEEN")) {
				strIndex+=6;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3))  
					    ,new Piece(Piece.PieceType.QUEEN));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+8).equals("SERGEANT")) {
				strIndex+=9;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.SERGEANT));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+5).equals("SCOUT")) {
				strIndex+=6;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.SCOUT));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+7).equals("PRIVATE")) {
				strIndex+=8;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.PRIVATE));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+7).equals("GENERIC")) {
				strIndex+=8;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.GENERIC));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+5).equals("EMPTY")) {
				strIndex+=6;
				
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.EMPTY));
				strIndex+=4;
	
			}
			else if(playerSetup.substring(strIndex, strIndex+5).equals("BLOCK")) {
				strIndex+=6;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.BLOCK));
				strIndex+=4;
			}
			else if(playerSetup.substring(strIndex, strIndex+4).equals("BOMB")) {
				strIndex+=6;
				Board.setPiece(Integer.parseInt(playerSetup.substring(strIndex,strIndex+ 1))
					    ,Integer.parseInt(playerSetup.substring(strIndex+2, strIndex+3)) 
					    ,new Piece(Piece.PieceType.BOMB));
				strIndex+=4;
			}
			
		}
		
	
	
	}

}
