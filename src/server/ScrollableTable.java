package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
 
/**
 * JTable with information about the connected clients
 * @author Aaron Roberts
 *
 */
public class ScrollableTable extends JPanel {

	public HashMap<Integer, SocketHandler> clients ;
	public JTable table;
	public Server mainServer;
	
	
	
	public ScrollableTable(HashMap<Integer, SocketHandler> hm, Server ms) {
		clients = hm;
		mainServer = ms;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 200));

        
        TableModel m = toTableModel(hm);
        table = new JTable(m);
    
        
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(800, 200));//800 is frames width, 200 is height

        
        JPopupMenu popupMenu = new JPopupMenu();
        
        
        JMenuItem deleteItem = new JMenuItem("Kick client");
        
        /** Action listener to kick the client **/
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	int v = table.getSelectedRow();
            	int value = (int) table.getValueAt(v, 0);

				ms.decrementClientCount(value);
				JOptionPane.showMessageDialog(table, "Client kicked off the server ");   	      
            }
        });
        
        /** Selects the row (left click) when you right click a row **/ 
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                        if (rowAtPoint > -1) {

                            table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        	int value = (int) table.getValueAt(rowAtPoint, 0);
                        	//ms.decrementClientCount(value);
                        	
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

		
			
        });
        popupMenu.add(deleteItem);
        
        table.setComponentPopupMenu(popupMenu);
        add(pane, BorderLayout.SOUTH);
	}
	
	/** Fill table model with entries from the hashmap of client connections**/
	public TableModel toTableModel(Map<?,?> map) {
	    DefaultTableModel model = new DefaultTableModel(
	        new Object[] { "Client ID", "Address" }, 0
	    );
	    for (Map.Entry<?,?> entry : map.entrySet()) {
	    	SocketHandler h = (SocketHandler) entry.getValue();
	        model.addRow(new Object[] { entry.getKey(), h.getAddress() });
	    }
	    return model;
	}
	
	/** Update table on change in clients connected **/
	public void updateClients(HashMap<Integer, SocketHandler> hm) {
		clients = hm;
		TableModel m = toTableModel(clients);
		table.setModel(m);
	
		table.repaint();
		this.repaint();
	}
	
}
