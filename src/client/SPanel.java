package client;

import client.screens.GameBoard;
import client.screens.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import java.awt.event.*;

public class SPanel extends JPanel implements ActionListener {

    public SPanel() {
        addKeyListener(new AAdapter());
        addMouseListener(new BAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        switch (Global.getGameState()) {
            case MENU:
                MainMenu.paintScreen(g, this);
                break;
            case GAME:
                GameBoard.paintScreen(g, this);
                break;
            default:
                MainMenu.paintScreen(g, this);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class AAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

        }

        public void keyReleased(KeyEvent e) {

        }
    }


    private class BAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {

        }
    }


}
