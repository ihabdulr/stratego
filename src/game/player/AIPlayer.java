package game.player;

import client.Global;
import game.Animation;
import game.Board;
import game.GameLogic;
import game.Piece;
import game.Piece.PieceType;
import game.SetupContainer;
import javafx.util.Pair;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static game.Board.SIZE_X;
import static game.Board.SIZE_Y;

/**
 * Created by Alek on 3/23/2018.
 */
public class AIPlayer extends GamePlayer {
	
	class pieceTracker {
		public Piece piece;
	    public int timesMoved;
	    public boolean known;
		public pieceTracker(Piece p, int i) {
			piece=p;
			timesMoved=i;
			known=false;
				}
		
	 
	}
	class bestmovemade{
		public Piece beststart;

		public Piece bestend;
		public  bestmovemade(Piece p,Piece e) {
			beststart=p;
			bestend=e;
		
		
				}
		
	 
	}

class bestmove {
	public Piece beststart;
	public int score;
	public Piece bestend;
	public  bestmove(Piece p,Piece e, int i) {
		beststart=p;
		bestend=e;
		score=i;
	
			}


}

	private static java.util.List<pieceTracker> guessedPieces = new ArrayList<pieceTracker>();
	private static int agresivep=0;
	private final static  int MAX_SCOUT=7;
	private final static int MAX_BOMB_DEFUSER=3;
	private final static  int MAX_MINION=6;
	private final static  int MAX_BOMB=3;
	private final static  int MAX_MINOTAUR=2;
	private final static   int MAX_SKULL_KING=1;
	private final static int MAX_SKULL_PRINCE=1;
	private final static  int MAX_FLAG=1;
	public static int turnnum;
   
	
	private static  HashMap<String, Integer> piececounter = new HashMap<>();

    public AIPlayer() {
    	
        this(0);
    }

    public AIPlayer(int mode) {
        if (mode == 0) {
            java.util.List<Piece.PieceType> pieces = SetupContainer.getGamePieces();
            Collections.shuffle(pieces);
            int flatMap = pieces.size();
            for (int x = 0; x < SIZE_X; ++x) {
                for (int y = 0; y < 3; ++y) {
                    myPieces.add(new Piece(pieces.get(flatMap - 1)).setPosition(x, y));
                    --flatMap;
                }
            }
            for (int x = 0; x < SIZE_X; ++x) {
                for (int y = 5; y < 8; ++y) {
                		pieceTracker temp= new pieceTracker(new Piece(Piece.PieceType.GENERIC).setPosition(x, y),0);
                		guessedPieces.add(temp);
                }
            }
        } else if (mode == 1) {
            myPieces.add(new Piece(Piece.PieceType.FLAG).setPosition(0, 3));
            myPieces.add(new Piece(Piece.PieceType.BOMB).setPosition(1, 3));
            myPieces.add(new Piece(Piece.PieceType.MINION).setPosition(0, 0));
        }
        
        
        
        piececounter.put(Piece.PieceType.FLAG.name(), 0);
        piececounter.put(Piece.PieceType.MINION.name(), 0);
        piececounter.put(Piece.PieceType.BOMB.name(), 0);
        piececounter.put(Piece.PieceType.MINOTAUR.name(), 0);
        piececounter.put(Piece.PieceType.SCOUT.name(), 0);
        piececounter.put(Piece.PieceType.SKULL_KING.name(), 0);
        piececounter.put(Piece.PieceType.SKULL_PRINCE.name(), 0);
        piececounter.put(Piece.PieceType.BOMB_DEFUSER.name(), 0);

    }
    public void intilizemap() {
    		  piececounter.put(Piece.PieceType.FLAG.name(), 0);
          piececounter.put(Piece.PieceType.MINION.name(), 0);
          piececounter.put(Piece.PieceType.BOMB.name(), 0);
          piececounter.put(Piece.PieceType.MINOTAUR.name(), 0);
          piececounter.put(Piece.PieceType.SCOUT.name(), 0);
          piececounter.put(Piece.PieceType.SKULL_KING.name(), 0);
          piececounter.put(Piece.PieceType.SKULL_PRINCE.name(), 0);
          piececounter.put(Piece.PieceType.BOMB_DEFUSER.name(), 0);
    	
    }
    ///here
    
    public java.util.List<java.util.List<Piece>> fakemove(Piece start,Piece end,java.util.List<Piece> enemypieces,java.util.List<Piece> mypieces){
    		for(	Piece piece: enemypieces) {
    			if(end.getPosition().equals(piece.getPosition()))
    				 return fakeattack(start,piece,enemypieces,mypieces );
    			
    		}
    		for(	Piece piece: mypieces) {
    			if(start.getPosition().equals(piece.getPosition())) {
    				piece.setPosition(end.getPosition());
    			break;
    			}
    			
    				
    			
    		}
    		java.util.List<java.util.List<Piece>> temp=new ArrayList<java.util.List<Piece>>();
    		temp.add(mypieces);
    		temp.add(enemypieces);
    		return temp;
    	    	
    	    		
    	    }
    
    
    private java.util.List<java.util.List<Piece>> fakeattack(Piece aPiece, Piece dPiece, List<Piece> enemypieces, List<Piece> mypieces) {

        

        if (dPiece.getPieceType().isPieceSpecial()) {
            switch (dPiece.getPieceType()) {
                case BOMB:
                    if (aPiece.getPieceType().equals(Piece.PieceType.BOMB_DEFUSER)) {
                    			
                    			for(Piece p: mypieces) {
                        			if(p.getPosition().equals(aPiece.getPosition()))
                        				p.setPosition(dPiece.getPosition());
                        		}
                        		
                    			enemypieces.remove(dPiece);
                    		
                    } else {
                    				mypieces.remove(aPiece);
                    }
                    break;
            }
        } else {
            if (aPiece.getPieceType().getCombatValue() == dPiece.getPieceType().getCombatValue()) {

                //Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
                //Board.setPiece(dPiece.getColumn(), dPiece.getRow(), new Piece(Piece.PieceType.EMPTY));

                mypieces.remove(aPiece);
                enemypieces.remove(dPiece);
               
              
            } else if (aPiece.getPieceType().getCombatValue() > dPiece.getPieceType().getCombatValue()) {
            		mypieces.remove(aPiece);
             
            } else {
            		for(Piece p: mypieces) {
            			if(p.getPosition().equals(aPiece.getPosition()))
            				p.setPosition(dPiece.getPosition());
            		}
            		
            		enemypieces.remove(dPiece);
                
             
                //Board.getCurrentPlayer().movePiece(aPiece, Board.getCurrentOpposingPlayer().getPiece(dPiece));


            }
        }
        java.util.List<java.util.List<Piece>> temp= new ArrayList<java.util.List<Piece>>();
        temp.add(mypieces);
        temp.add(enemypieces);
        return temp;
      
    }


	public enum moveUpdate{MOVE,ATTACKLOSS,ATTACKWIN,TIE}
	public enum Turn{AI,LOCAL}
    //guessed enemy pices by movement 
    
    public static List<Piece> guessesBoard(){
    		
    		List<Piece> guessedp= new ArrayList<Piece>();
    		for(pieceTracker pt: guessedPieces) {
    			if(!pt.piece.getPieceType().equals(Piece.PieceType.GENERIC)) {
    				guessedp.add(pt.piece);
    			
    				piececounter.put(pt.piece.getPieceType().name(), piececounter.get(pt.piece.getPieceType().name())+1);
    			}
    			else {
    				Piece temp= pt.piece.clone();
    				
    				if((MAX_SKULL_KING-piececounter.get(Piece.PieceType.SKULL_KING.name()))>0) {
    					piececounter.put(Piece.PieceType.SKULL_KING.name(), piececounter.get(Piece.PieceType.SKULL_KING.name())+1);
    					temp.setPieceType(Piece.PieceType.SKULL_KING);
    					guessedp.add(temp);
    				}
    				else if((MAX_SKULL_PRINCE-piececounter.get(Piece.PieceType.SKULL_PRINCE.name()))>0) {
    					piececounter.put(Piece.PieceType.SKULL_PRINCE.name(), piececounter.get(Piece.PieceType.SKULL_PRINCE.name())+1);
    					temp.setPieceType(Piece.PieceType.SKULL_PRINCE);
    					guessedp.add(temp);
    				}
    				else if((MAX_MINOTAUR-piececounter.get(Piece.PieceType.MINOTAUR.name()))>0) {
    					piececounter.put(Piece.PieceType.MINOTAUR.name(), piececounter.get(Piece.PieceType.MINOTAUR.name())+1);
    					temp.setPieceType(Piece.PieceType.MINOTAUR);
    					guessedp.add(temp);
    				}
    				else if((MAX_BOMB_DEFUSER-piececounter.get(Piece.PieceType.BOMB_DEFUSER.name()))>0) {
    					piececounter.put(Piece.PieceType.BOMB_DEFUSER.name(), piececounter.get(Piece.PieceType.BOMB_DEFUSER.name())+1);
    					temp.setPieceType(Piece.PieceType.BOMB_DEFUSER);
    					guessedp.add(temp);
    				}
    				else if((MAX_MINION-piececounter.get(Piece.PieceType.MINION.name()))>0) {
    					piececounter.put(Piece.PieceType.MINION.name(), piececounter.get(Piece.PieceType.MINION.name())+1);
    					temp.setPieceType(Piece.PieceType.MINION);
    					guessedp.add(temp);
    				}
    				else if((MAX_SCOUT-piececounter.get(Piece.PieceType.SCOUT.name()))>0) {
    					piececounter.put(Piece.PieceType.SCOUT.name(), piececounter.get(Piece.PieceType.SCOUT.name())+1);
    					temp.setPieceType(Piece.PieceType.SCOUT);
    					guessedp.add(temp);
    				}
    				else if((MAX_BOMB-piececounter.get(Piece.PieceType.BOMB.name()))>0) {
    					piececounter.put(Piece.PieceType.BOMB.name(), piececounter.get(Piece.PieceType.BOMB.name())+1);
    					temp.setPieceType(Piece.PieceType.BOMB);
    					guessedp.add(temp);
    				}
    				else if((MAX_FLAG-piececounter.get(Piece.PieceType.FLAG.name()))>0) {
    					piececounter.put(Piece.PieceType.FLAG.name(), piececounter.get(Piece.PieceType.FLAG.name())+1);
    					temp.setPieceType(Piece.PieceType.FLAG);
    					guessedp.add(temp);
    				}
    	
    			}
    				
    			 
    			
    			
    		}
    	
    		return guessedp;
    		
			
    }
    
    public int Scorer(java.util.List<Piece> aipieces,java.util.List<Piece> playerpieces,int depth) {
    		int score =aipieces.size() -playerpieces.size();
    		
    		/*boolean flag=false;
    		for(Piece p: playerpieces) {
    			if(p.getPieceType().equals(Piece.PieceType.FLAG)) {
    				flag=true;
    			}
    		}
    		if(flag==false)
    			return 1000;
    		boolean aiflag=false;
    		for(Piece p: aipieces) {
    			if(p.getPieceType().equals(Piece.PieceType.FLAG)) {
    				aiflag=true;
    			}
    		}
    		if(aiflag==false)
    			return -1000;
    		*/
    		
    		return score;
    }
    //best move for ai
    public bestmovemade getBestmove(java.util.List<Piece> aipieces,java.util.List<Piece> playerpieces,int depth){
    	
    		
    		int current;
    		bestmovemade bm = null;
    		int max=Integer.MIN_VALUE;
    		int min=Integer.MAX_VALUE;
    		Collections.shuffle(aipieces);
    		for(Piece piece: aipieces) {
				java.util.List<Piece> temp=new ArrayList<Piece>();
				temp.addAll(fakegetMovableTiles(piece, aipieces, playerpieces));
				if(!temp.isEmpty()) {
					for(Piece p: temp) {
						
						java.util.List<java.util.List<Piece>> listtemp= new ArrayList<java.util.List<Piece>>();
						java.util.List<Piece> atemplist=new ArrayList<Piece>();
						for(Piece t: aipieces) {
							atemplist.add(t.clone());
						}
						java.util.List<Piece> ptemplist=new ArrayList<Piece>();
						for(Piece t: playerpieces) {
							ptemplist.add(t.clone());
						}
						
						listtemp=fakemove(piece, p, ptemplist, atemplist);
					
						current=min(listtemp.get(0), listtemp.get(1), depth-1);
						//System.out.println(max);
						
						if(max<current) {
							max=current;
							bm=new bestmovemade(piece,p);
							
						}
							
						
						
					}
				
				}
			
			}
    		
    	
    		return bm;
    }
    
    public int max(java.util.List<Piece> aipieces,java.util.List<Piece> playerpieces, int depth) {
    	//System.out.println(depth);
    		if(depth==0)
			return Scorer(aipieces,playerpieces,depth); 
    	
		int max=Integer.MIN_VALUE;
		//HashMap<Piece,java.util.List<Piece> > aimoves = new HashMap<>();
		for(Piece piece: aipieces) {
			java.util.List<Piece> temp=fakegetMovableTiles(piece, aipieces, playerpieces);
			if(!temp.isEmpty()) {
				for(Piece p: temp) {
					java.util.List<java.util.List<Piece>> listtemp= new ArrayList<java.util.List<Piece>>();
					java.util.List<Piece> atemplist=new ArrayList<Piece>();
					for(Piece t: aipieces) {
						atemplist.add(t.clone());
					}
					java.util.List<Piece> ptemplist=new ArrayList<Piece>();
					for(Piece t: playerpieces) {
						ptemplist.add(t.clone());
					}
						
					
					listtemp=fakemove(piece, p, ptemplist, atemplist);
					
					int current =min(listtemp.get(0), listtemp.get(1), depth-1);
					
					if(current>=max) {
						max=current;
					}
				}
				
			}
	}
	return max;	
    	
    }
    
    
    
    
    public int min(java.util.List<Piece> aipieces,java.util.List<Piece> playerpieces, int depth) {
   // 	System.out.println(depth);
    		if(depth==0)
    			return Scorer(aipieces,playerpieces,depth); 
    	
		int min=Integer.MAX_VALUE;
		//HashMap<Piece,java.util.List<Piece> > aimoves = new HashMap<>();
		for(Piece piece: aipieces) {
			java.util.List<Piece> temp=fakegetMovableTiles(piece, playerpieces, aipieces);
			if(!temp.isEmpty()) {
				for(Piece p: temp) {
					java.util.List<java.util.List<Piece>> listtemp= new ArrayList<java.util.List<Piece>>();
					java.util.List<Piece> atemplist=new ArrayList<Piece>();
					for(Piece t: aipieces) {
						atemplist.add(t.clone());
					}
					java.util.List<Piece> ptemplist=new ArrayList<Piece>();
					for(Piece t: playerpieces) {
						ptemplist.add(t.clone());
					}
						
					
					listtemp=fakemove(piece, p, atemplist, ptemplist);
					
					int current=max(listtemp.get(1), listtemp.get(0), depth-1);
					if(current<min) {
						min=current;
					}
				}
				
			}
	}
	return min;	
    	
    }

	
    
    
    
    
    
    
    public static void updatetracker(Piece p,Piece pd,moveUpdate move) {
    	
    		if(move.equals(moveUpdate.ATTACKWIN)) {
    			for(pieceTracker pt: guessedPieces) {
    				if(pt.piece.getPosition().equals(p.getPosition())) {
    					pt.piece.setPieceType(p.getPieceType()).setPosition(pd.getPosition());
    					pt.known=true;
    					int cdist=Math.abs((p.getColumn()-pd.getColumn()));
           			int rdist=Math.abs((p.getRow()-pd.getRow()));
           			if(cdist>rdist)
           				pt.timesMoved=cdist;
           			else
           				pt.timesMoved=rdist;	
    					break;
    				}
    				}	
    			}
    		else	 if(move.equals(moveUpdate.ATTACKLOSS)||move.equals(moveUpdate.TIE)) {
    			for(pieceTracker pt: guessedPieces) {
    				if(pt.piece.getPosition().equals(p.getPosition())) {
    					guessedPieces.remove(pt);
    					break;
    				}
    					
    				}
    			}
    		else if(move.equals(moveUpdate.MOVE)) {
    			for(pieceTracker pt: guessedPieces) {
    				if(pt.piece.getPosition().equals(p.getPosition())) {
    					pt.piece.setPosition(pd.getPosition());
    					int cdist=Math.abs((p.getColumn()-pd.getColumn()));
               			int rdist=Math.abs((p.getRow()-pd.getRow()));
               			if(cdist>rdist) {
               				pt.timesMoved+=cdist;
               				if(cdist>1) {
               					pt.piece.setPieceType(Piece.PieceType.SCOUT);
               					pt.known=true;
               				}
               			}
               			else {
               				pt.timesMoved+=rdist;
               				if(rdist>1) {
               					pt.piece.setPieceType(Piece.PieceType.SCOUT);
               					pt.known=true;
               				}
               			}
               			
    					break;
    				}
    				
    				
    				}
    			}
		
			Collections.sort(guessedPieces, new Comparator<pieceTracker>(){
				@Override
				public int compare(pieceTracker o1, pieceTracker o2) {
					// TODO Auto-generated method stub
					if((o1.known==true&&o2.known==true))
						return 0;
					
					else if(o2.known==true)
						return 1;
					else if(o1.known==true)
						return -1;
					else if (o2.timesMoved==o1.timesMoved)
						return 0;
					else
					return o1.timesMoved > o2.timesMoved ? -1 : 1;
				}
			});
	
						
    	
    }

    @Override
    public boolean hasAtLeastOneMovablePiece() {
        return getPieces().stream().anyMatch(p -> !GameLogic.getMovableTiles(p).isEmpty());
    }

    @Override
    public boolean removePiece(Piece piece) {
        System.out.println("Removing AI Piece: " + piece.getPieceType().name());
        Board.addCapturedPiece(piece.getPieceType());
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(piece.getPosition())).findAny();
        if (p.isPresent()) {
            Board.setPiece(piece.getColumn(), piece.getRow(), new Piece(Piece.PieceType.EMPTY));
            System.out.println("Size: " + myPieces.size());
            myPieces.remove(p.get());
            System.out.println("Size: " + myPieces.size());

            System.out.println("f: " + hasAtLeastOneMovablePiece());
            return true;
        } else {
            System.out.println("Error finding piece in AI");
            return false;
        }
    }

    @Override
    public boolean movePiece(Piece aPiece, Piece dPiece) {
    	
        Board.setPiece(dPiece.getColumn(), dPiece.getRow(), dPiece.setPieceType(Piece.PieceType.GENERIC));
        Board.setPiece(aPiece.getColumn(), aPiece.getRow(), new Piece(Piece.PieceType.EMPTY));
        Optional<Piece> p = myPieces.stream().filter(i -> i.getPosition().equals(aPiece.getPosition())).findAny();
        if (p.isPresent()) {
            myPieces.get(myPieces.indexOf(p.get())).setPosition(dPiece.getColumn(), dPiece.getRow());
            return true;
        }
        System.out.println("Failed to move " + dPiece.getPieceType().name() + " to " + aPiece.getPosition().toString());
        return false;
    }


    public boolean nextMove() {
    		intilizemap();
    		turnnum++;
    		java.util.List<Piece> tempenemy= guessesBoard();
    		java.util.List<Piece> tempai = new ArrayList<Piece>();
    		//System.out.println(myPieces.size());
    		for(Piece p: myPieces) {
    			tempai.add(p.clone());
    		}
    		
    		//bestmove bm= miniMax(tempai, tempenemy, 1, null, null, Turn.AI, -1000);
    		bestmovemade bmade= getBestmove(tempai,tempenemy,1);
    		Board.TurnState state = Board.move(bmade.beststart, bmade.bestend);
    		System.out.println("Enemy moving: " + bmade.beststart.getPieceType() + " to " + bmade.bestend.getPieceType()+" "+bmade.bestend.getPosition()+bmade.beststart.getPosition());
            if(state.equals(Board.TurnState.VALID)) { 
                Global.setBoardState(Global.BoardState.MY_TURN);
                return true;
            }
            else
                Global.setBoardState(Global.BoardState.GAME_WON);
       /* for (Piece piece : myPieces) {
            java.util.List<Piece> available = GameLogic.getMovableTiles(piece);
            
            if (!available.isEmpty()) {
                Board.TurnState state = Board.move(piece, available.get(0));
                System.out.println("Enemy moving: " + piece.getPosition() + " to " + available.get(0).getPosition());
                if(state.equals(Board.TurnState.VALID)) {
                    Global.setBoardState(Global.BoardState.MY_TURN);
                    return true;
                }
                else
                    Global.setBoardState(Global.BoardState.GAME_WON);

            }
        }
        */
        Global.setBoardState(Global.BoardState.GAME_WON);
        return false;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public static java.util.List<Piece> fakegetMovableTiles(Piece piece,java.util.List<Piece> mypieces,java.util.List<Piece> epieces) {
        java.util.List<Piece> pieces = new ArrayList<Piece>();
        if (piece != null) {
            if(piece.getPieceType().getCombatValue() == -1)
                return pieces;
            int mx = piece.getColumn();
            int my = piece.getRow();
            for (Board.Direction dir : Board.Direction.values()) {
                for (int i = 0; i < piece.getMaxDistance(); ++i) {
                    int dx = dir.equals(Board.Direction.LEFT) || dir.equals(Board.Direction.RIGHT) ? mx + (dir.getOffset() * (i + 1)) : mx;
                    int dy = dir.equals(Board.Direction.UP) || dir.equals(Board.Direction.DOWN) ? my + (dir.getOffset() * (i + 1)) : my;
                    if (dx >= 0 && dy >= 0 && dx < SIZE_X && dy < SIZE_Y) {

                        Piece p=new Piece(Piece.PieceType.EMPTY).setPosition(dx,dy);
                        Piece temp1= new Piece(Piece.PieceType.BLOCK).setPosition(3,3);
                        Piece temp2= new Piece(Piece.PieceType.BLOCK).setPosition(4,3);
                        Piece temp3= new Piece(Piece.PieceType.BLOCK).setPosition(3,4);
                        Piece temp4= new Piece(Piece.PieceType.BLOCK).setPosition(3,4);
                			//temp.setPosition(dx, dy);
                        for(Piece ptemp:  mypieces) {
                        		if(ptemp.getPosition().equals(p.getPosition()))
                        			p=ptemp;
                        }
                        //enemy has piece
                        boolean has=false;
                        for(Piece ptemp:  epieces) {
                    			if(ptemp.getPosition().equals(p.getPosition())) {
                    				has=true;
                    				p=ptemp;
                    			}
                    }
                      if(p.getPosition().equals(temp1.getPosition())
                    		  ||p.getPosition().equals(temp2.getPosition())
                    		  ||p.getPosition().equals(temp3.getPosition())
                    		  ||p.getPosition().equals(temp4.getPosition()))
                    	  	p.setPieceType(Piece.PieceType.BLOCK);
                        
                        

                         if (p.getPieceType().equals(Piece.PieceType.EMPTY) || has==true)
                                pieces.add(p);

                        if (!p.getPieceType().equals(Piece.PieceType.EMPTY))
                            break;

                    }

                }
            }
        }
        return pieces;
    }


}


 
    
    
    
    
    
    
    
    


