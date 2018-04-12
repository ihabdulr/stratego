package client.screens;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.sound.sampled.Clip;

import client.Global;
import client.Global.GameState;
import client.resources.Images;
import client.resources.Sound;
import server.Packets;

public class MainMenu implements Screen {

    public Rectangle closeButton = new Rectangle(625, 375, 25, 25);
    public MenuButton button_pvp = new MenuButton("Play vs Player", 350, 300, 300, 50);
    public MenuButton button_ai = new MenuButton("Play vs AI", 350, 400, 300, 50);
    public MenuButton button_quit = new MenuButton("Exit Game", 350, 500, 300, 50);
    public static MenuButton buttonPressed = null;
    public static MenuButton buttonHovered = null;
    public Color hoverColor = new Color(19, 78, 17, 80); // Used for hovering
    public Color darkGreen = new Color(10, 40, 8, 80);
    private String searchStatus = "";

    public void processMouseMovedEvent(MouseEvent e) {
        buttonHovered = null;
        if (button_pvp.getBounds().contains(e.getPoint())) {
            buttonHovered = button_pvp;
        }
        if (button_quit.getBounds().contains(e.getPoint())) {
            buttonHovered = button_quit;
        }
        if (button_ai.getBounds().contains(e.getPoint())) {
            buttonHovered = button_ai;
        }
    }


    Font font = new Font("Sans-Serif", Font.BOLD, 25);
    public void paintScreen(Graphics g, ImageObserver o) {

        button_pvp.setFontMetrics(g.getFontMetrics(font));
        button_ai.setFontMetrics(g.getFontMetrics(font));
        button_quit.setFontMetrics(g.getFontMetrics(font));

        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(Images.getImage("background_1"), 0, 0, o);
        g.drawImage(Images.getImage("logo"), 185, 130, o);

        g2d.setColor(darkGreen);
        g2d.fillRect((int) button_pvp.getX(), (int) button_pvp.getY(), (int) button_pvp.getWidth(),
                (int) button_pvp.getHeight());
        g2d.fillRect((int) button_ai.getX(), (int) button_ai.getY(), (int) button_ai.getWidth(),
                (int) button_ai.getHeight());
        g2d.fillRect((int) button_quit.getX(), (int) button_quit.getY(), (int) button_quit.getWidth(),
                (int) button_quit.getHeight());

        // Hover button

        if (button_pvp.equals(buttonHovered)) {
            g2d.setColor(hoverColor);
            g2d.fillRect((int) button_pvp.getX(), (int) button_pvp.getY(), (int) button_pvp.getWidth(),
                    (int) button_pvp.getHeight());
        }
        if (button_ai.equals(buttonHovered)) {
            g2d.setColor(hoverColor);
            g2d.fillRect((int) button_ai.getX(), (int) button_ai.getY(), (int) button_ai.getWidth(),
                    (int) button_ai.getHeight());
        }
        if (button_quit.equals(buttonHovered)) {
            g2d.setColor(hoverColor);
            g2d.fillRect((int) button_quit.getX(), (int) button_quit.getY(), (int) button_quit.getWidth(),
                    (int) button_quit.getHeight());
        }

        
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(button_pvp.getText(), button_pvp.getStringX(), button_pvp.getStringY());
        g2d.drawString(button_ai.getText(), button_ai.getStringX(), button_ai.getStringY());
        g2d.drawString(button_quit.getText(), button_quit.getStringX(), button_quit.getStringY());

        g2d.setColor(Color.WHITE);
        g2d.drawString(button_pvp.getText(), button_pvp.getStringX() + 2, button_pvp.getStringY()+ 2);
        g2d.drawString(button_ai.getText(), button_ai.getStringX()+ 2, button_ai.getStringY()+ 2);
        g2d.drawString(button_quit.getText(), button_quit.getStringX()+ 2, button_quit.getStringY()+ 2);

        // To draw an image, we use ImageObserver like this:
        // g2d.drawImage(Images.loadImage("test.png"), 50, 175, o);

        // When vs player is clicked "searching for player" window appears
        if (button_pvp.equals(buttonPressed)) {

            MenuButton searchMenu = new MenuButton(searchStatus, 350, 375, 300, 200);
            g2d.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
            g2d.setColor(Color.BLUE);
            g2d.fillRect((int) searchMenu.getX(), (int) searchMenu.getY(), (int) searchMenu.getWidth(),
                    (int) searchMenu.getHeight());
            searchMenu.setFontMetrics(g.getFontMetrics(new Font("Sans-Serif", Font.PLAIN, 15)));
            g2d.setColor(Color.WHITE);
            searchStatus = "Searching for a player...";
            g2d.drawString(searchStatus, searchMenu.getStringX(), searchMenu.getStringY());

            g.drawImage(Images.getImage("close_background_"), 0, 0, o);

            g2d.drawImage(Images.loadImage("closeicon"), closeButton.x, closeButton.y, closeButton.width,
                    closeButton.height, o);

        }

    }

    public void processMousePressedEvent(MouseEvent e) {
        // vs Player buttton
    	System.out.println("Board state: "+Global.getBoardState()+" | GameState: "+Global.getGameState());

        if (button_pvp.getBounds().contains(e.getPoint()) && (buttonPressed == null || buttonPressed == button_pvp)) {
            // Global.setGameState(GameState.GAME);

            Clip clip = Sound.loadSound("buttonclickon");
            clip.setMicrosecondPosition(0);
           // clip.start();

            buttonPressed = button_pvp;
            if (Global.connectedServer.connect())
                Global.connectedServer.addCommand(Packets.P_QUEUE_PLAYER);
            else
                searchStatus = "Failed to connect to the server";
        } else if (button_ai.getBounds().contains(e.getPoint())
                && (buttonPressed == null || buttonPressed.equals(button_ai))) {
            Global.setGameState(GameState.GAME);
            buttonPressed = button_ai;
            Clip clip = Sound.loadSound("buttonclickon");
            clip.setMicrosecondPosition(0);
           // clip.start();
        }
        // quit button
        else if (button_quit.getBounds().contains(e.getPoint())
                && (buttonPressed == null || buttonPressed.equals(button_quit))) {
            System.exit(0);
        }
        // close button on "searching for player" box
        else if (closeButton.getBounds().contains(e.getPoint()) && (buttonPressed.equals(button_pvp))) {
            Clip clip = Sound.loadSound("buttonclickoff");
            clip.setMicrosecondPosition(0);
           // clip.start();
            if (Global.connectedServer.isConnected()) {
                Global.connectedServer.removeCommand(Packets.P_QUEUE_PLAYER);
                Global.connectedServer.addCommand(Packets.P_REMOVE_FROM_QUEUE);
                Global.connectedServer.disconnect();
            }
            buttonPressed = null;
        }

    }

}
