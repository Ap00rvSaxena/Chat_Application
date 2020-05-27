/*
 * Full Name: Apoorv Saxena 
 * UTA ID: 1001681737
 * Built on skeleton code from https://github.com/SrihariShastry/socketProgramming/blob/master/src/lab1/Client.java 
 */

package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {

	// Input Streams for sending out data between Server-Client
	private ObjectInputStream inputStream; //Reading from Socket
	// Output Streams for receiving out data between Server-Client
	private ObjectOutputStream outputStream; //Writing to Socket 
	// Socket to pass data 
	private Socket socket;						

	ClientGUI clientGUI;							// Client GUI for User Interaction
	
	private String server = "localhost";	// Server address
	public String username = "NewClient";	// Initial client username to interact with server
	private int port = 3000;				//Server port address

	/*
	 * Constructor call to initialize client object with user name and client GUI instance
	 */
	Client(String username, ClientGUI clientGUI) {
		this.username = username;
		this.clientGUI = clientGUI;
	}
	
	/*
	 * Constructor overloading to only initialize and reference  client GUI instance
	 */
	Client(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}
	
	/*
	 * gives out current client/object username
	 */
	public String getUsername() {
		return username;
	}
	
	/*
	 * set the username of the current object/client
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * Start conversation with the server
	 */
	public boolean startClient() {
		// Defining server using ipaddress and post address
		try {
			socket = new Socket(server, port);
		} 
		// throw error message if fails
		catch(Exception ec) {
			showMessage("Error connectiong to server:" + ec);
			return false;
		}
		
		// Letting user know on client GUI for successful connection
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		showMessage(msg);
	
		/* Creating Data Streams for input and output*/
		try
		{
			inputStream  = new ObjectInputStream(socket.getInputStream());
			outputStream= new ObjectOutputStream(socket.getOutputStream());
		}
		//throw error message if fails
		catch (IOException eIO) {
			showMessage("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// making a Thread to handle server response
		new ServerReply().start();
		
		// send the username to server to register the newclient
		try
		{
			outputStream.writeObject(username);
		}
		//throw error message if fails
		catch (IOException eIO) {
			showMessage("Exception doing login : " + eIO);
			// disconnect the client if any error arises
			disconnect();
			return false;
		}
		
		// notify client that the connection was successful
		return true;
	}

	/*
	 * appends MSG in the text field in Client GUI  
	 */
	private void showMessage(String msg) {
			clientGUI.updateClientLog(msg);		
	}
	
	/*
	 * Send message to client(s) through the socket outputstream
	 */
	void sendMessage(String[] msg) {
		try {
			outputStream.writeObject(msg);
		}
		catch(IOException e) {
			showMessage("Exception writing to server: " + e);
		}
	}

	/*
	 * Disconnect user when user promt or any error arises 
	 */
	private void disconnect() {
		try { 
			if(inputStream != null) inputStream.close();
			if(outputStream != null) outputStream.close();
			if(socket != null) socket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
			
	}

	/*
	 * continiously wait for reply from server and parse response 
	 */
	class ServerReply extends Thread {
		public void run() {
			while(true) {
				try {
					String[] response = new String[6];
					
					response = (String[]) inputStream.readObject();
					
					// get the content type to determine if a package is 
					// message if message than broadcast, unicast or multicast 
					// or server sent the all client list to clients.					
					String contentType = response[2].substring(13);
					String guiusername = response[3];
					
					// if message then sent the msg to client doesn't matter if broadcast, unicast or multicast 
					if(contentType.trim().equalsIgnoreCase("broadcast")|| contentType.trim().equalsIgnoreCase("multicast") || contentType.trim().equalsIgnoreCase("unicast")) {
						clientGUI.updateClientLog(response[3]);
						clientGUI.updateClientLog(response[6]);
					}
					// to let client know whether current client already exists in server client list or not
					else if (contentType.trim().equalsIgnoreCase("username")){
						//reject user if client is exists in list and already connected
						// ALert the user about this case
						if (response[6].equalsIgnoreCase("rejected")){
							clientGUI.setValidUser(false);
							clientGUI.setOldUser(true);
							clientGUI.infoBox("Username: " + guiusername + " is Already Connect, choose another", "Alert");
						}
						// accept user if client is doesn't exists in list
						// ALert the user about this case
						else if(response[6].equalsIgnoreCase("accepted")){
							clientGUI.setValidUser(true);
							clientGUI.setOldUser(false);
							//enable the client to send messages
							clientGUI.btnSendMessage.setEnabled(true);
							clientGUI.infoBox("User: " + guiusername + " is Connected", "Alert");
						}
						// accept user if client exists in list and is not connected than connect the user 
						// ALert the user about this case
						else{
							clientGUI.setValidUser(true);
							clientGUI.setOldUser(true);
							//enable the client to send messages
							clientGUI.btnSendMessage.setEnabled(true);
							clientGUI.infoBox("Welcome back " + guiusername + "!", "Alert");
						}
						clientGUI.loginUser();
					}
					// update the client about the current client list on server
					else if (contentType.trim().equalsIgnoreCase("client-list")) {
						clientGUI.updateClientList(response);
					}
					// update cliets logs screen when requested for old message file
					else if (contentType.trim().equalsIgnoreCase("message-file")) {
						System.out.println("Message response from server: " + response[6].length());
						if (response[6].length() == 0)
							response[6] += "There are No messages in the queue.\n";  
						clientGUI.getMsgLogFromServer(response[6]);
					}
				}
				// if connection failed alreat the user 
				catch(Exception e) {
					showMessage("Server has close the connection: " + e);
					if(clientGUI != null) 
						clientGUI.connectionFailed();
					break;
				}
				
			}
		}
	}	
}
