# Chat Application!

A real-time GUI based chat application is implemented using **Java** and **multi-threading** where communication takes place over **socket** connection and data flows bi-directionally between Server and multiple Clients, also configured persistent queue to prevent data loss and provide offline message storage capability.

## Instructions on compiling and running the project:

IDE Used: **Eclipse**
Language: **Java 8**

-  Import on to the eclipse IDE by the import project function under file menu. Import it as Java project.
- Run ServerMain.java, located in main package, Click the Start button to start the server.
- The server starts to listen for client on localhost:3000.
- Then run ClientMain.java file located in main package which will implement the client side of the application then click connect button to connect to the server located at localhost:3000.
- Type in the desired username and login to server. To start sending messages to other clients.
- Repeat Step 4 to 5, three time to generate 3 clients.
- Client can input already used username, if server find conflict it will notify the client and prompt it to enter new if required. 
- Follow the GUI to navigate further to send different types of messages to clients i.e broadcast, unicast or multicast messages to other client.
- Choose the message type from the list of client mentioned on ClientGUI, also you can track active and disconnected user on ServerGUI.
- Press the Get messages button on client gui to get all the message sent to it, even when it was not online.
- Disconnect the user anytime by pressing disconnect button, which will also update the client table on server.
- In order to stop Server press Stop button.
