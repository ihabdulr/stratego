package client;

import client.screens.MainMenu;
import game.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import java.awt.event.*;

public class SPanel extends JPanel implements ActionListener {

    private MainMenu mainMenu;
    private Board board;

    public SPanel() {
        board = new Board();
        mainMenu = new MainMenu();
        addKeyListener(new AAdapter());
        BAdapter b = new BAdapter();
        addMouseListener(b);
        addMouseMotionListener(b);

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
                mainMenu.paintScreen(g, this);
                break;
            case GAME:
                board.paintScreen(g, this);
                break;
            default:
                mainMenu.paintScreen(g, this);
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

        public void mouseMoved(MouseEvent e){
            switch (Global.getGameState()) {
                case MENU:
                    mainMenu.processMouseMovedEvent(e);
                    break;
                case GAME:
                    board.processMouseMovedEvent(e);
                    break;
             }
            repaint();
        }

        public void mousePressed(MouseEvent e) {
            switch (Global.getGameState()) {
                case MENU:
                    mainMenu.processMousePressedEvent(e);
                    break;
                case GAME:
                    board.processMousePressedEvent(e);
                    break;
            }
            	repaint();
        }
    }


}
