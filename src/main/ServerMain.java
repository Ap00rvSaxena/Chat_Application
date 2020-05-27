/**
 * Full Name: Apoorv Saxena 
 * UTA ID: 1001681737
 */
package main;

import java.awt.EventQueue;

import server.ServerGUI;

/**
 * @author Apoorv
 *
 */
public class ServerMain {

	/**
	 * Launches Server GUI
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frmServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
