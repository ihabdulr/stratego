package client.screens;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

/**
 * Created by Alek on 3/6/2018.
 */
public interface Screen {

    void paintScreen(Graphics g, ImageObserver o);
    void processMousePressedEvent(MouseEvent e);
    void processMouseMovedEvent(MouseEvent e);
}
