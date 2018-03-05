import server.Server;
import server.ServerWindow;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ServerDriver  {

	public static int PORT = 22344;
	public static int TIME_OUT = 0;
	public static Server mainServer;
	
	public static JFrame mainFrame;
	public static void main(String[] args) {
		

		mainServer = new Server(PORT, TIME_OUT);
		
		Thread ms = new Thread(mainServer);
		
		SwingUtilities.invokeLater( new Runnable() {

			@Override
			public void run() {
				 mainFrame = new ServerWindow(mainServer);
				 ms.start();
			}
		});
	}


}
