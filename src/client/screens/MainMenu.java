package client.screens;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by Alek on 3/2/2018.
 */
public class MainMenu {

    public static void paintScreen(Graphics g, ImageObserver o) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.drawString("Stratego 2442", 20, 50);
        //To draw an image, we use ImageObserver like this:
        //g2d.drawImage(Images.loadImage("test.png"), 50, 175, o);

    }


}
