package client.screens;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import client.Global;
import client.Global.GameState;
import client.SPanel;
import client.resources.Images;
import game.Board;

/**
 * Created by Alek on 3/2/2018.
 */
public class MainMenu implements Screen{
	
		public Rectangle closeButton= new Rectangle(625, 375,25,25);
		public MenuButton button1=new MenuButton("vs Player",400,300,200,50);
		public MenuButton button2=new MenuButton("vs AI",400,400,200,50);
		public MenuButton button3=new MenuButton("Quit",400,500,200,50);
		public MenuButton button4=new MenuButton("Quit",400,500,200,50);
		public MenuButton buttonpressed=null;
		
		
		
    public void paintScreen(Graphics g, ImageObserver o) {
    
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("ZapfDingbats", Font.PLAIN, 50));
        g2d.drawString("Stratego 2442", 325, 150);
        
        g2d.setColor(Color.RED);
        g2d.fillRect((int)button1.getX(),(int)button1.getY(), (int)button1.getWidth(), (int)button1.getHeight());
        g2d.fillRect((int)button2.getX(),(int)button2.getY(), (int)button2.getWidth(), (int)button2.getHeight());
        g2d.fillRect((int)button3.getX(),(int)button3.getY(), (int)button3.getWidth(), (int)button3.getHeight());
        
        
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g2d.drawString(button1.getText(), (float)button1.getX()+50,(float) button1.getY()+35);
        g2d.drawString(button2.getText(), (float)button2.getX()+65,(float) button2.getY()+35);
        g2d.drawString(button3.getText(), (float)button3.getX()+75,(float) button3.getY()+35);
        
        
        //To draw an image, we use ImageObserver like this:
        //g2d.drawImage(Images.loadImage("test.png"), 50, 175, o);
    
        //When vs player is clicked "searching for player" window appears
        if(button1==buttonpressed) {
       		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        		g2d.setColor(Color.BLUE);
        		g2d.fillRect(350, 375, 300, 200);
        		g2d.setColor(Color.WHITE);
        		g2d.drawString("Searching for Players....", 425, 450);
    
        		g2d.drawImage(Images.loadImage("closeicon"), closeButton.x, closeButton.y,closeButton.width,closeButton.height,o);
        }
        		
        		
      
       
        

    }

    public void processEvent(MouseEvent e) {
    			
    		
   //vs Player buttton
    	if((e.getX()>button1.getX()&&e.getX()<button1.getX()+button1.getWidth())&&(buttonpressed==null||buttonpressed==button1)) {
    		if(e.getY()>button1.getY()&&e.getY()<button1.getY()+button1.getHeight()) {
    			//Global.setGameState(GameState.GAME);
    			buttonpressed= button1;
    
    		}
    	}
    	//vs AI button
    if((e.getX()>button2.getX()&&e.getX()<button2.getX()+button2.getWidth())&&(buttonpressed==null||buttonpressed==button2)) {
    		if(e.getY()>button2.getY()&&e.getY()<button2.getY()+button2.getHeight()) {
    			Global.setGameState(GameState.GAME);
    			buttonpressed= button2;
    		}
   }
    	//quit button
     if((e.getX()>button3.getX()&&e.getX()<button3.getX()+button3.getWidth())) {
		if(e.getY()>button3.getY()&&e.getY()<button3.getY()+button3.getHeight()) {
			System.exit(0);
		}
    	}
   
     
    //close button on "searching for player" box
    	 if((e.getX()>closeButton.getX()&&e.getX()<closeButton.getX()+closeButton.getWidth())&&(buttonpressed==button1)) {
    		if(e.getY()>closeButton.getY()&&e.getY()<closeButton.getY()+closeButton.getHeight()) {
    			buttonpressed=null;
    			
    		}
        	}
    
    	

    }
    
    


}
