package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Main jframe for the server
 * @author Aaron Roberts
 *
 */

public class ServerWindow extends JFrame {
	
	public Server mainServer;
	public JTextArea console;
	public JScrollPane scroll;
	public JLabel label1;

	public ServerWindow(Server s) {
		mainServer = s;
		mainServer.setFrame(this);
		this.setName("Stratego 2442 - Server");
		this.setTitle("Stratego 2442 - Server");
		this.setSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		ScrollableTable st = new ScrollableTable(s.getClients(), mainServer);
		JPanel newPane = st;
		
		label1 = new JLabel("Clients Connected: " +mainServer.getNumberOfConnections());
	
		/** Console log for server **/
		JTextArea consoleLog = new JTextArea();
		consoleLog.setEditable(false);
		consoleLog.setAutoscrolls(true);
		console = consoleLog;
		scroll = new JScrollPane(consoleLog); 
		
		/** Add components to panel **/
		newPane.add(scroll, BorderLayout.CENTER);
		newPane.add(label1, BorderLayout.NORTH);
		
		this.setContentPane(newPane);
	}
	
	public void updateTable() {
		JPanel newPane = new ScrollableTable(mainServer.getClients(), mainServer);
		
		label1 = new JLabel("Clients Connected: " +mainServer.getNumberOfConnections()); //Update client count

		/** Add components to panel **/
		newPane.add(scroll, BorderLayout.CENTER);
		newPane.add(label1, BorderLayout.NORTH);
		
		this.setContentPane(newPane);
		this.validate();
		this.repaint();
	}
	
	/** Writes to console **/
	public void writeToConsole(String s) {
		console.append(s + "\n");
	}
	
}
