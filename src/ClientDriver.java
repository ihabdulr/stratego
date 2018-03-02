import javax.swing.*;

import client.Network;
import client.SPanel;

public class ClientDriver extends JFrame {

    //Server-Client communication
    public static int PORT = 22344;
    public static String SERVER = "localhost";
    public static Network mainClient;


    public ClientDriver() {
        add(new SPanel());
        setTitle("Stratego 2442");
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setVisible(true);

    }

    public static void main(String[] args) {
        //mainClient = new Network(PORT, SERVER);
       new ClientDriver();
    }
        



}
