/*
 * Full Name: Apoorv Saxena 
 * UTA ID: 1001681737
 * Built on skeleton code from https://github.com/SrihariShastry/socketProgramming/blob/master/src/lab1/ClientGUI.java 
 */

package client;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JList;

public class ClientGUI implements ActionListener{

	public JFrame frmClient;
	public JTextField txtMessage;
	public JTextField txtUsername;
	Client client;
	public JTextArea txtrReplies;
	public JButton btnConnect;
	public JButton btnSendMessage;
	public JButton btnDisconnect;
	public JButton btnRefresh;
	public JButton btnLogin;
	public boolean connected;
	public boolean validUser;
	public boolean oldUser;
	public String guiUsername;
	public DefaultListModel listModel;
	public JList jList;
	public JButton btnGetMsgs;

	/*
	 * Getter function to know whether current user is an existing user or new user
	 */
	public boolean isOldUser() {
		return oldUser;
	}

	/*
	 * Setter function to know whether current user is an existing user or new user
	 */
	public void setOldUser(boolean oldUser) {
		this.oldUser = oldUser;
	}
	
	/*
	 * Getter function to know whether current user is an exists and is connected to server
	 */
	public boolean isValidUser() {
		return validUser;
	}
	
	/*
	 * Getter and setter function to know whether current user is an exists and is connected to server
	 */
	public void setValidUser(boolean validUser) {
		this.validUser = validUser;
	}

	/**
	 * Initialize UI elements of the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setTitle("Client");
		frmClient.setResizable(false);
		frmClient.setBounds(100, 100, 472, 448);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(this);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);		
		
		txtMessage = new JTextField();
		txtMessage.setColumns(20);
		
		txtUsername = new JTextField();
		txtUsername.setEditable(false);
		txtUsername.setColumns(10);
		
		JLabel lblUserName = new JLabel("User Name");
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(this);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(this);
		btnRefresh.setVisible(false);
		
		JLabel lblUserName_1 = new JLabel("Message");
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(this);
		btnLogin.setEnabled(false);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		btnGetMsgs = new JButton("Check for messages");
		btnGetMsgs.addActionListener(this);
		btnGetMsgs.setEnabled(false);
		GroupLayout groupLayout = new GroupLayout(frmClient.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblUserName)
									.addGap(18)
									.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblUserName_1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnGetMsgs, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
										.addComponent(txtMessage, 283, 283, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDisconnect, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addComponent(btnRefresh))
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
								.addComponent(btnSendMessage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(149)
							.addComponent(btnConnect, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)))
					.addGap(34))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(btnConnect, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName)
						.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLogin))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSendMessage))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName_1)
						.addComponent(btnRefresh)
						.addComponent(btnGetMsgs))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(26, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
							.addComponent(btnDisconnect)
							.addGap(38))))
		);
		
		listModel = new DefaultListModel();
		jList = new JList(listModel);
		jList.setVisibleRowCount(5);
		scrollPane_1.setViewportView(jList);
		
		txtrReplies = new JTextArea();
		scrollPane.setViewportView(txtrReplies);
		frmClient.getContentPane().setLayout(groupLayout);
		/*
		 * Only Connection Button is enable at start up
		 */
		btnConnect.setEnabled(true);		// connect to server
		btnDisconnect.setEnabled(false);		// cant disconnect when the client has not connected yet
		btnRefresh.setEnabled(false);		// cant request for 
		btnSendMessage.setEnabled(false);		// cant send message without logging in
	}

	/*
	 * Put message in the text area to let client know of messages it is receiving
	 */
	public void updateClientLog(String msg) {
		txtrReplies.append("\n" + msg);
	}
	
	/*
	 * NEVER USED
	 * Puts the old message from user message storage file in
	 * client logs
	 */
	public void getMsgInLogFromClient(String username){
		try
        {
            FileWriter writer = new FileWriter(".//src//server//"+ username+ ".txt");
            BufferedWriter bw = new BufferedWriter( writer );
            txtrReplies.write(bw);
            bw.close();
            txtrReplies.setText("");
            txtrReplies.requestFocus();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	/*
	 * Puts the old message from user message storage file in
	 * client logs
	 */
	public void getMsgLogFromServer(String content){
        txtrReplies.setText("");
        txtrReplies.setText(content);
        txtrReplies.requestFocus();
	}
	
	
	/*
	 * On connection failure, message, login button and text area are disabled
	 */
	void connectionFailed() {
		btnConnect.setEnabled(true);
		btnDisconnect.setEnabled(false);
		btnRefresh.setEnabled(false);
		txtMessage.setText("Enter your username");
		connected = false;
	}

	/*
	 * Defines what specific task various button performs 
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();			// get source button
		
		String[] request = new String[7];	// request string
		
		// Client decides to Logout
		if(o == btnDisconnect) {
			// Form a Headers to send all relevent information about client and user intention
			request[0] = "DELETE / HTTP/1.1";
			request[1] = "Host: localhost";
			request[2] = "User-Agent: " + client.getUsername();
			request[3] = "Content-Type: Text";
			request[4] = "Content-Length =  0";
			request[5] = "\r\n";
			request[6] = " ";
			client.sendMessage(request);
			
			//	handle UI changes after disconnect
			btnConnect.setEnabled(true);
			btnDisconnect.setEnabled(false);
			btnRefresh.setEnabled(false);
			txtMessage.setEditable(false);
			txtUsername.setEditable(true);
			btnRefresh.doClick();
			return;
		}
		// Used this invisible button to perform the task of active client list update from user  
		else if(o == btnRefresh) {
			request[0] = "GET CLIENTLIST HTTP/1.1";
			request[1] = "Host: localhost";
			request[2] = "User-Agent: " + client.getUsername();
			request[3] = "Content-Type: client-list";
			request[4] = "Content-Length =  0";
			request[5] = "\r\n";
			request[6] = " ";
			client.sendMessage(request);
			return;
		}
		// Check if message is broadcast(1-all), unicast(1-1) or multicast(1-n) selected from the list option from GUI
		else if(o==btnSendMessage) {			
			System.out.println("print selected: " + jList.getSelectedValuesList());
			// total number of list item selected to send the message
			int numRecipients =  jList.getSelectedValuesList().size();
			// total elements in the client list
			int totalRecipients = jList.getModel().getSize();
			//usernames of all the user that the client intended to send message to
			String multiTo = "";
			// list of string which represent client pick for usr to send msg to
			List<String> selectedlist =  jList.getSelectedValuesList();
			//convert list string to string in order to be sent over the outputstream over socket
			multiTo = selectedlist.stream()
				      .map(n -> String.valueOf(n))
				      .collect(Collectors.joining(";", "", ""));
				
			System.out.println("jList Items: " + multiTo + " No of Recp: " + numRecipients);
			
			//made specific header allowing us to send information regarding recipeints of the msg
			//sender of the msg
			// type of msg broadcast, multicast, unicast
			// and msg itself
			request[0] = "POST HTTP/1.1";
			request[1] = "Host: localhost";
			request[2] = "User-Agent: " + multiTo;
			request[3] = "Content-Type: broadcast";
			request[4] = "Content-Length = " + txtMessage.getText().length();
			request[5] = "\r\n";
			request[6] = client.getUsername();
			
			//indicate if the msg is supposed to broadcast, multicast or unicasted over the inputstream
			// and change header accordingly
			// also the message
			if(multiTo.contains("broadcast")){
				request[6] += " (1-all): "+ txtMessage.getText();
			}
			else if (numRecipients == 1){
				request[3] = "Content-Type: unicast";
				request[4] = "From: " + client.getUsername();
				request[6] += " (1-1) : " +txtMessage.getText();
			} 
			else if(numRecipients > 1){
				request[3] = "Content-Type: multicast";
				request[4] = "From: " + client.getUsername();
				request[6] += " (1-n): "+txtMessage.getText();
			}
			
			//send the message over socket
			client.sendMessage(request);
			//empty the message area for new message to be written.
			txtMessage.setText("");
			return;
		}
		//Connect button connects the client to server to share msg across other clients
		else if(o == btnConnect) {
			// create a new client object which handles reponses from server.
			client = new Client(this);
			// start the client thread which continiously listens to the server for responses.
			if(!client.startClient()) 
				return;
			// empty the message area for new message to be typed.
			txtMessage.setText("");
			
			// connected status is true.
			connected = true;
			//UI elemets are to be set after connection
			btnConnect.setEnabled(false);
			btnLogin.setEnabled(true); 
			btnDisconnect.setEnabled(true);
			btnRefresh.setEnabled(true);
			txtUsername.setEditable(true);
			txtMessage.setEnabled(true);			
		}
		//Login the current user using only usrname name which must not exists in server current active client list
		else if(o == btnLogin) {
			guiUsername = txtUsername.getText().trim();
			
			// if username or if port number is empty, do nothing.
			if(guiUsername.length() == 0)
				return;
			
			// Make the appropriate header to send out this information over socket
			// defining content type and the actual message ie usrname.
			String to = "server";
			request[0] = "PUT HTTP/1.1";
			request[1] = "Host: localhost";
			request[2] = "User-Agent: " + to;
			request[3] = "Content-Type: username";
			request[4] = "Content-Length = " + txtUsername.getText().length();
			request[5] = "\r\n";
			request[6] = guiUsername;
			
			
			//send the message
			client.sendMessage(request);
			//empty the message area for new message to be written.
			txtMessage.setText("");			
			client.setUsername(guiUsername);
			return;
		}
		//Pressing this button loads up previous stored messages in the file.
		else if(o == btnGetMsgs) {
			// Generate reswues to be sent to server
			request[0] = "GET MESSAGES HTTP/1.1";
			request[1] = "Host: localhost";
			request[2] = "User-Agent: " + client.getUsername();
			request[3] = "Content-Type: message-file";
			request[4] = "Content-Length =  0";
			request[5] = "\r\n";
			request[6] = " ";
			System.out.println("GetMsg Button Pressed");
			//request sent to server
			client.sendMessage(request);
			//getMsgInLogFromClient(client.getUsername());
		}

	}
	
	// function to determine if current client username already exists on server list or not
	// if exists is it disconnected or not then only allows user to send out msg across socket
	// else reject usr and promt for another user name
	// raise alret according to specific results
	public void loginUser(){
		//user is not an old user and is not connected
		if (this.oldUser && this.validUser){
			client.setUsername(guiUsername);
			btnLogin.setEnabled(false); 
			btnDisconnect.setEnabled(true);
			btnRefresh.setEnabled(true);
			txtUsername.setEditable(false);
			btnGetMsgs.setEnabled(true);
			btnRefresh.doClick();
			frmClient.setTitle("Client: " + guiUsername + " Logged In");
		}
		// user is an old user and is not connected
		else if (!this.oldUser && this.validUser){
			System.out.println("Before d: " + client.username);
			System.out.println("Before f: " + client.getUsername());
			client.setUsername(guiUsername);
			System.out.println("After d: " + client.username);
			System.out.println("After f: " + client.getUsername());
			//infoBox("User: " + guiUsername + " is Connected", "Alert");
			btnLogin.setEnabled(false); 
			btnDisconnect.setEnabled(true);
			btnRefresh.setEnabled(true);
			txtUsername.setEditable(false);
			frmClient.setTitle("Client:" + guiUsername + " Logged In");
			btnGetMsgs.setEnabled(true);
			btnRefresh.doClick();
		}
		// user is an old user and is connected
		else{
			//infoBox("Username: " + guiUsername + " is Already Connect, choose another", "Alert");
			return;
		}
	}
	
	// https://stackoverflow.com/questions/7080205/popup-message-boxes
	//Raises alert about username authentications
	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

	/*
	 * updates the client GUI list which shows the active client connected to server
	 */
	public void updateClientList(String[] response) {
		
		// split the response to get individual clients
		String[] list = response[6].split(",");
		// update the client list dropdown
		listModel.clear();
		for (String s : list)
			listModel.addElement(s);
	}
}
