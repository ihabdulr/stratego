package client.screens;


import java.awt.*;

import game.BoardButton;

public class MenuButton {
		private Rectangle rect;
		private String buttonText;
		private int stringX;
		private int stringY;
	
		
      public MenuButton(String buttonName,int x, int y, int width, int height) {
         buttonText = buttonName;
         rect=new Rectangle(x,y,width,height);
         //stringX = x + (width - metrics.stringWidth(buttonText)) / 2;
        // stringY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
       }
      public void setFontMetrics(FontMetrics metrics) {
          stringX = rect.x + (rect.width - metrics.stringWidth(buttonText)) / 2;
          stringY = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
          
      }
      public Rectangle getBounds() {
    	  	return rect;
      }

      public int getStringX() {
          return stringX;
      }

      public int getStringY() {
          return stringY;
      }

      public double getX() {
  	  	return rect.getX();
    }
      public double getY() {
    	  	return rect.getY();
      }
      public double getWidth(){
  	  	return rect.getWidth();
    }
      public double getHeight() {
  	  	return rect.getHeight();
    }
      
      public String getText() {
    	 	return buttonText;
     }

}
