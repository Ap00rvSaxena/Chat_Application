/*
 * Full Name: Apoorv Saxena 
 * UTA ID: 1001681737
 * Built on skeleton code from https://github.com/SrihariShastry/socketProgramming/blob/master/src/lab1/Server.java 
 */

package server;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Server {
	ServerGUI serverGUI;
	// post of address for client ot connect on
	int port = 3000;
	//List to identify all the current client connected too server
	ArrayList<CThread> ClientList;
	//Each client will have specific and unique clinetID
	int clientID;
	//determine whether server is still runnning or not
	private boolean runT;
	//Map to assertain that not repeatative client connect to server 
	//or no duplicate user name exists which are simultaniously connected to server
	HashMap<String, String> activeClient;
	//File to access and store usernames 
	File usernameFile;
	//allows to read and write in random fashion
	RandomAccessFile raf;
	//tells us if we have found the file or not
	boolean fileFound;	
	
	// Getter function for current active client list that are connected or disconnected
	public HashMap<String, String> getActiveClient() {
		return activeClient;
	}

	// Setter function for current active client list that are connected or disconnected
	public void setActiveClient(HashMap<String, String> activeClient) {
		this.activeClient = activeClient;
	}

	/*
	 * Initialize server GUI and client list
	 */
	public Server(ServerGUI serverGUI) {
		// GUI object
		this.serverGUI = serverGUI;
		// ArrayList for the Client list
		this.ClientList = new ArrayList<CThread>();
		// Initialize Client ID
		clientID = 0;
		//initialize the active client that are connected or disconnected
		activeClient = new HashMap<>();
		usernameFile = new File(".\\src\\server\\usernames.txt");
	}
	
	// Continuously listen to the socket for connection requests from clients
	public void startServer() {
		runT = true;
		try 
		{
			//Read Username from txt file to know previous clients
			readUserNameInMap(activeClient, usernameFile);
			serverGUI.updateTable(activeClient);
			System.out.println(activeClient);
			
			// socket used by server
			ServerSocket serverSocket = new ServerSocket(port);

			// continiously wait for request
			while(runT) 
			{
				Socket socket = serverSocket.accept();  	// accept connection	
				// notify that we are waiting for connection requests
				display("Listening for Clients on "+ socket.getInetAddress() + ":" + socket.getPort()+"\n");
				
				
				// if server was to shut down
				if(!runT)
					break;
				
				// Create thread for each client
				CThread cThread = new CThread(socket);  
				//add client to client list
				ClientList.add(cThread);	
				//start the thread and listen to it coniniously 
				cThread.start();
			}
			// if server shuts down  close inout and output streams
			try {				
				//close the client thread input and output streams of every client
				for(int i = 0; i < ClientList.size(); ++i) {
					CThread cThread = ClientList.get(i);
					try {
						cThread.inputStream.close();
						cThread.outputStream.close();
						cThread.socket.close();
					}
					catch(IOException ioE) {
						ioE.printStackTrace();
					}
				}
				serverSocket.close(); 	// close the server socket
			}
			
			//raise errors to usr if any arises
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		//raise error to user if server crash
		catch (IOException e) {
            String msg = " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}
	
	/*
	 * append server messages on text area to let the user know about activities of clients
	 */
	private void display(String msg) {
		serverGUI.txtrServerLog.append(msg+"\n");
	}
	
	/*
	 * Method user bufferReader to extract information from username.txt
	 * which contains current and previous user status i.e Connected or disconnected
	 * That is making server statefull
	 */
	//https://stackoverflow.com/questions/29061782/java-read-txt-file-to-hashmap-split-by
	public void readUserNameInMap(HashMap<String, String> map, File fileName){ 
		try {
		//to read file line by line
		String line;
		//used to reader whole file
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null)
			{
				//Split key and value pairs and store them in activelist hashmap
			    String[] parts = line.split(":", 2);
			    if (parts.length >= 2)
			    {
			        String key = parts[0];
			        //initally all the user will be disconnected
			        String value = "Disconnected";//parts[1];
			        map.put(key, value);
			    } else {
			        System.out.println("ignoring line: " + line);
			    }
			}
		//Debug step to print out the hashmap generated through reading username.txt file
	    for (String key : map.keySet())
	    {
	        System.out.println(key + ":" + map.get(key));
	    }
	    //close out the bufferReader
	    reader.close();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	
	/*
	 * Method user bufferWriter to extract information from activelist hashmap
	 * which contains user status i.e Connected or disconnected and writing them in username.txt
	 * That is making server statefull
	 */
	//https://www.javacodeexamples.com/write-hashmap-to-text-file-in-java-example/2353
	public void writeUsernamesInFile(HashMap<String, String> map, File fileName){
		BufferedWriter bf = null;
        try{  
            //create new BufferedWriter for the output file
            bf = new BufferedWriter( new FileWriter(fileName) );
 
            //iterate map entries
            for(Entry<String, String> entry : map.entrySet()){
                
                //put key and value separated by a colon
                bf.write( entry.getKey() + ":" + entry.getValue() );
                
                //new line
                bf.newLine();
            }
            //flush the writer
            bf.flush();
 
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            
            try{
                //close the writer
                bf.close();
            }catch(Exception e){}
        }
	}
	
	
	/*
	 *Stop the server  
	 */
	protected void stop() {
		//set run Thread to false to notify that the server has crashed 
		runT = false;
		writeUsernamesInFile(activeClient, usernameFile);
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * create one thread to handle every client
	 */
	
	class CThread extends Thread {
		//socket, input output streams 
		Socket socket;
		ObjectInputStream inputStream;
		ObjectOutputStream outputStream;
		
		// unique id for each client. 
		int id;
		
		// the user of the Client
		String user;

		// Constructor
		CThread(Socket socket) {
			// unique id for each client
			id = ++clientID;
			this.socket = socket;
			try
			{
				// creating  data streams
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				inputStream  = new ObjectInputStream(socket.getInputStream());
				
				// read the user
				user = (String) inputStream.readObject();
				// notify that the user connected
				display(user + " just connected");
			}
			catch (IOException e) {
				// if failed to create I/O data streams
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * returns the user for the particular client thread
		 */
		public String getUserName() {
			return user;
		}
		
		/*
		 * sets the user for the particular client thread
		 */
		public void setUserName(String user) {
			this.user = user;
		}

		/*
		 * append messages on server Log to be displayed to users
		 */
		private void display(String string) {
				serverGUI.updateLog(string+ "\n");
			
		}
		
		/*
		 * Server send Response to the client
		 */
		private boolean respond(String[] respose) {
			// check if socket is connected
			if(!socket.isConnected()) {
				close();
				return false;
			}
			
			// if connected, then write to the output datastream
			try {
				outputStream.writeObject(respose);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		/*
		 * continiously listen to the client requests
		 */
		public void run() {
			// Continiously listen to the cient for any requests
			boolean runT = true;
			while(runT) {
				// read the request from client and return specific responses to it.
				String[] request = new String[7];		// request from client
				String[] response = new String[7];		// response from server
				try {
					//read client request
					request = (String[]) inputStream.readObject();	
					//get the current date and time for response 
					Date date = new Date();
				    String strDateFormat = "DD/MM/YYYY hh:mm:ss a";
				    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				    // get the date and time in the format
				    String formattedDate= dateFormat.format(date);
				    
				    //prepare response by adding appropriate headers 				    
					response[0] = "HTTP/1.1 200 OK";
					response[1] = "Server: localhost";
					response[2] = "Content-type: message";
					response[3] = "Date: " + formattedDate;
					response[4] = "Content-Length: " + request[5].length();
					response[5] = "\r\n";
					response[6] = "";
					
					//determining if client asks for client list
					//using content type or just HTTP method
					if(request[0].contains("GET") && request[3].contains("client-list")) {
						response[2] = "Content-type: client-list";
						// add all usernames to the list and send the response
						for(Entry ct : activeClient.entrySet()) {
							response[6] += ct.getKey() + ",";
						}
						// add broadcast to the list
						response[6] += "broadcast";
						
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine += s + " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						
						//broadcast over all the client
						for(CThread ct : ClientList) {
//							send message to all clients
							if(!ct.respond(response))
								ClientList.remove(ct);	// if sending message fails then remove the client from client list
							}
					
					}
					
					//determining whether clinet is trying to login with username
					//check username with the active client list on the server 
					//resopnding to client based on whether user name exists on active list or not
					else if(request[0].contains("PUT")&&(request[3].contains("username"))) {
						
						//get the current client name to communicate to
						String userDest = "NewClient"; 
						//get current Client thread
						CThread currCT = null;
						for(CThread ct : ClientList) {
							if(ct.getUserName().equalsIgnoreCase(userDest)) {
								currCT = ct;
								break;
							}
						}
						
						// client requested usrname
						String newusr = request[6];
						//server response to that usrname
						//whethter accepted or rejected
						String resp = "accepted";
						//checking if the usrname exists in active client list
						// if exists than is it connected or disconnected
						if (activeClient.containsKey(newusr)){
							if (activeClient.get(newusr).equalsIgnoreCase("Connected"))
								resp = "rejected";
							else{
								resp = "accepted again";
								activeClient.put(newusr, "Connected");
								currCT.setUserName(newusr);
							}
						}
						else{
							activeClient.put(newusr, "Connected");
							currCT.setUserName(newusr);
						}
						request[6] = resp;
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s+ " ";
						}
						
						//message to be sent
						response[2] = "Content-type: username";
						response[6] = request[6];
						response[3] = newusr;
						//send the response to client
						currCT.respond(response);	
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine + ": Connection from "+ newusr);
						serverGUI.updateTable(activeClient);
						respond(response);
					}
					
					// if client sends a 1-ALL message i.e Broadcast
					else if (request[0].contains("POST")&&(request[3].contains("BROADCAST")||request[3].contains("broadcast"))) 
					{
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s+ " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						
						//get the message to be sent inside the response data
						response[2] = request[3];
						response[6] = request[6];
						for (Entry entry : activeClient.entrySet()){
							String tempusername = entry.getKey().toString();
							writeMsgToUserFile(tempusername, response[3]);
							writeMsgToUserFile(tempusername, response[6]);
						}
						for(CThread ct : ClientList) {
							// send message to all clients
							if(!ct.respond(response))
								ClientList.remove(ct);	// if sending message fails then remove the client from client list
							}
						
					}
					
					// if client sends a 1-1 message unicast
					else if(request[0].contains("POST") && (request[3].contains("unicast"))) {
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s+ " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						
						//get the destination client name
						String userDest = request[2].substring(11).trim();
						System.out.println("Server unicast: " + (userDest));
						//message to be sent
						response[2] = request[3];
						String sender = request[4].substring(5).trim();
						response[6] = request[6];
						//Store the message in respective client message file
						writeMsgToUserFile(userDest, response[3]);
						writeMsgToUserFile(userDest, response[6]);
						writeMsgToUserFile(sender, response[3]);
						writeMsgToUserFile(sender, response[6]);
						//check client list for destination client and send message 
						for(CThread ct : ClientList) {
							if(ct.getUserName().equalsIgnoreCase(userDest)) {
								ct.respond(response);
							}
							if(ct.getUserName().equalsIgnoreCase(sender)) {
								ct.respond(response);
							}
						}
					}
					// if client sends a 1-N message i.e multicast
					else if(request[0].contains("POST") && (request[3].contains("multicast"))) {
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s+ " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						//get the destination client name
						String[] userDest = request[2].substring(11).trim().split(";"); 
						System.out.println("Server multi: " + Arrays.toString(userDest));
						response[2] = request[3];
						//message to be sent
						String sender = request[4].substring(5).trim();
						response[6] = request[6];
						
						//Store the message in respective clients message file
						for (String tempusername : userDest){
							writeMsgToUserFile(tempusername, response[3]);
							writeMsgToUserFile(tempusername, response[6]);
						}
						
						//check client list for destination client and send message
						for (String s : userDest ){
							for(CThread ct : ClientList) {
								System.out.println("This is multicast: " + s + " User:" +ct.getUserName()+" ->");
								System.out.print(ct.getUserName().equalsIgnoreCase(s)+"\n");
								if(ct.getUserName().equalsIgnoreCase(s)) {
									ct.respond(response);
								}
							}
						}
					}
					
					// if client requests logout delete it from active client list and map
					else if(request[0].contains("DELETE")) {
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s+ " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						
						// get the user of the client who wants to logout
						String userAgent = request[2].substring(12);
						
						//remove the respective client from the list by closing input and output streams
						Iterator it = ClientList.iterator();
						while(it.hasNext()) {
							CThread ct = (CThread) it.next();
							if(userAgent.equalsIgnoreCase(ct.getUserName())) {
								activeClient.put(ct.getUserName(), "Disconnected"); // change the value of username to disconnected whos logging out 
								it.remove();
							}
						}
						// update the active client table on Server GUI
						serverGUI.updateTable(activeClient);
					}
					//if client request to load older message content of the respective usermessage file is returned
					else if(request[0].contains("GET") && request[3].contains("message-file")) {
						//user requested info/ use to give response to
						String userDest = request[2].substring(12);
						//reading out content from the respective usermessage file
						String fileContent = readMsgsFromUserFile(userDest);
						//Debug step to printout read info from file
						System.out.println("Reading msg file: Content:\n" + fileContent);
						response[2] = request[3];
						//sending file content as response
						response[6] = fileContent;
						
						//unparsed HTTP request
						String requestLine= "";
						for(String s: request) {
							requestLine+=s + " ";
						}
						//print unparsed HTTP request on server log
						serverGUI.updateLog(requestLine);
						
						//sending the response to respective client
						for(CThread ct : ClientList) {
							if(ct.getUserName().equalsIgnoreCase(userDest)) {
								ct.respond(response);
							}
						}
					
					}
				}
				// raise error if any
				catch (IOException e) {
					display(user + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
			}
			// remove myself from the arrayList containing the list of the
			// connected Clients 
			ClientList.remove(id-1);
			close();
		}
		
		// closing all I/O streams
		private void close() {
			try {
				if(outputStream != null) outputStream.close();
				if(inputStream != null) inputStream.close();
				if(socket != null) socket.close();
				serverGUI.updateLog("\n" + user + "Disconnected\n" );
				activeClient.put(user, "Disconnected");
				serverGUI.updateTable(activeClient);
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		// logout user remove 
		synchronized void remove(int id) {
			// 
			for(CThread ct : ClientList ) {
				if(ct.id == id) {
					ClientList.remove(ct);
					return;
				}
			}
		}
		
		/*
		 * This method allows server to save every message to respective user
		 * message storage file; i.e appending each message sent to that user
		 * makes server persistent as user whose not online can view message
		 * sent to it, when it comes back online
		 */
		public void writeMsgToUserFile(String username, String msg){
			try{
				
				//Create the new file for storing messages for user
				File file = new File(".//src//server//"+ username+ ".txt");
				if (file.createNewFile())
				    System.out.println("File is created!");
				else
				    System.out.println("File already exists.");
				//Filewriter to open respective usermessage file
				FileWriter fr = new FileWriter(file, true);
				//bufferwriter to write to respective usermessage file
				BufferedWriter br = new BufferedWriter(fr);
				//printwriter to print to respective usermessage file				
				PrintWriter pr = new PrintWriter(br);
				pr.println(msg);
				//closes printer
				pr.close();
				//closes buffer
				br.close();
				//closes filewriter
				fr.close();
			}
			catch(IOException e){
	            e.printStackTrace();
	        }
		}
		
		/*
		 * This method allows server to read every message sent to respective user
		 * stored in its repsective file and then sent those message as respone 
		 * to that user when it comes back online thus making server persistent 
		 * as user whose not online can view message
		 */
		//https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
		public String readMsgsFromUserFile(String username){
			//reads out the content of the file and build a string out of it
			StringBuilder contentBuilder = new StringBuilder();
		    try{
		    	//point to file in the system
		    	File file = new File(".//src//server//"+ username+ ".txt");
				//creates file if doesn't exsists
		    	if (file.createNewFile())
				    System.out.println("File is created!");
				else
				    System.out.println("File already exists.");
		    	//to read out content from file
		    	BufferedReader br = new BufferedReader(new FileReader(file));
		        //store thos content line wise line, appending
		    	String sCurrentLine;
		        while ((sCurrentLine = br.readLine()) != null) 
		        {
		            contentBuilder.append(sCurrentLine).append("\n");
		        }
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		    //update Server that queue has been cleared
		    display("Queue has been cleared");
		    //return whole content of the file as a string
		    return contentBuilder.toString();
		}
	}
}