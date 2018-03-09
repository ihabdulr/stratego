package client.screens;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class MenuButton {
		private Rectangle rect;
		private String buttonText;
		
      public MenuButton(String buttonName,int x, int y, int width, int height) {
         buttonText = buttonName;
         rect=new Rectangle(x,y,width,height);
       }
      public Rectangle getBounds() {
    	  	return rect;
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
