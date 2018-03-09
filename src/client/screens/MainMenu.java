package client.screens;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import client.Global;
import client.Global.GameState;
import client.resources.Images;

public class MainMenu implements Screen {

    public Rectangle closeButton = new Rectangle(625, 375, 25, 25);
    public MenuButton button_pvp = new MenuButton("vs Player", 400, 300, 200, 50);
    public MenuButton button_ai = new MenuButton("vs AI", 400, 400, 200, 50);
    public MenuButton button_quit = new MenuButton("Quit", 400, 500, 200, 50);
    public MenuButton buttonPressed = null;

    public void paintScreen(Graphics g, ImageObserver o) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("ZapfDingbats", Font.PLAIN, 50));
        g2d.drawString("Stratego 2442", 325, 150);

        g2d.setColor(Color.RED);
        g2d.fillRect((int) button_pvp.getX(), (int) button_pvp.getY(), (int) button_pvp.getWidth(), (int) button_pvp.getHeight());
        g2d.fillRect((int) button_ai.getX(), (int) button_ai.getY(), (int) button_ai.getWidth(), (int) button_ai.getHeight());
        g2d.fillRect((int) button_quit.getX(), (int) button_quit.getY(), (int) button_quit.getWidth(), (int) button_quit.getHeight());


        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g2d.drawString(button_pvp.getText(), (float) button_pvp.getX() + 50, (float) button_pvp.getY() + 35);
        g2d.drawString(button_ai.getText(), (float) button_ai.getX() + 65, (float) button_ai.getY() + 35);
        g2d.drawString(button_quit.getText(), (float) button_quit.getX() + 75, (float) button_quit.getY() + 35);


        //To draw an image, we use ImageObserver like this:
        //g2d.drawImage(Images.loadImage("test.png"), 50, 175, o);

        //When vs player is clicked "searching for player" window appears
        if (button_pvp.equals(buttonPressed)) {
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g2d.setColor(Color.BLUE);
            g2d.fillRect(350, 375, 300, 200);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Searching for Players....", 425, 450);

            g2d.drawImage(Images.loadImage("closeicon"), closeButton.x, closeButton.y, closeButton.width, closeButton.height, o);
        }

    }

    public void processEvent(MouseEvent e) {
        //vs Player buttton
        if (button_pvp.getBounds().contains(e.getPoint()) && (buttonPressed == null || buttonPressed == button_pvp)) {
            //Global.setGameState(GameState.GAME);
            buttonPressed = button_pvp;
        } else if (button_ai.getBounds().contains(e.getPoint()) && (buttonPressed == null || buttonPressed == button_ai)) {
            Global.setGameState(GameState.GAME);
            buttonPressed = button_ai;
        }
        //quit button
        else if (button_quit.getBounds().contains(e.getPoint())) {
            System.exit(0);
        }
        //close button on "searching for player" box
        else if (closeButton.getBounds().contains(e.getPoint()) && (buttonPressed == button_pvp)) {
            buttonPressed = null;
        }

    }


}
