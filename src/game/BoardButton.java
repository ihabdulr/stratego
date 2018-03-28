package game;

import java.awt.*;

/**
 * Created by Alek on 3/15/2018.
 */
public class BoardButton extends Rectangle {

    private String buttonText;
    private int stringX, stringY;
    private boolean highlighted = false;
    private boolean enabled = true;


    BoardButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.buttonText = text;
    }

    public BoardButton setFontMetrics(FontMetrics metrics) {
        stringX = super.x + (super.width - metrics.stringWidth(buttonText)) / 2;
        stringY = super.y + ((super.height - metrics.getHeight()) / 2) + metrics.getAscent();
        return this;
    }

    public int getStringX() {
        return stringX;
    }

    public int getStringY() {
        return stringY;
    }

    public String getString() {
        return buttonText;
    }

    public void setHighlighted(boolean value) {
        highlighted = value;
    }

    public BoardButton setEnabled(boolean value) {
        enabled = value;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHighlighted() {
        return highlighted;
    }



}
