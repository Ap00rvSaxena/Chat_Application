/**
 * Full Name: Apoorv Saxena 
 * UTA ID: 1001681737 
 */
package main;

import java.awt.EventQueue;

import client.ClientGUI;

/**
 * @author Apoorv
 *
 */
public class ClientMain {

	/**
	 * Launch the Client GUI.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
