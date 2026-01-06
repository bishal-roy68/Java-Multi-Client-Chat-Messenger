 /*
 * Final day updated the code from Bishal
 Reference: used partial code idea from textbook...
 * */

package server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


 public class ClientHandler extends Thread {

    private Socket clientSocket;
    private int clientId;
    private PrintWriter out;
    private String username; // default name if client does not send one


    // setting up the socket for this client and saving its ID
    public ClientHandler(Socket socket, int id) {
        this.clientSocket = socket;
        this.clientId = id;
    }

     private String getTime() {
         return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
     }

     // sending to only one client
     public void sendMessage(String msg) {
         out.println(msg);
     }


     @Override
    public void run() {
        try {

            // sending messages back to this client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // reading messages coming from the client
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            // first message from client should be the username
            username = in.readLine();
            System.out.println("[" + getTime() + "] Client " + clientId + " joined as: " + username);

            // letting everyone know someone joined
            broadcastToEveryone(" " + username + " joined the chat. ");

            String message;

            // reading all client messages
            while ((message = in.readLine()) != null) {

                // printing to server console
                System.out.println("[" + getTime() + "] " + username + ": " + message);

                // sending message to all users
                broadcastToEveryone(username + ": " + message);

            }

        } catch (Exception e) {
            System.out.println("[" + getTime() + "] Client " + clientId + " (" + username + ") disconnected.");
        } finally {

            // remove them from the list
            ChatServer.clientList.remove(this);

            // tell everyone someone left
            broadcastToEveryone(" " + username + " left the chat - " + getTime());
        }
    }

     // sending message to all clients
     private void broadcastToEveryone(String msg) {

         for (ClientHandler handler : ChatServer.clientList.toArray(new ClientHandler[0])) {

             try {
                 handler.sendMessage(msg);

                 // just printing this so we know the message actually reached the client
                 System.out.println("[Delivered -> Client " + handler.clientId + "] " + msg);

             } catch (Exception ex) {

                 // if a client is not responding, remove them
                 System.out.println("[Remove] Client " + handler.clientId + " is not responding.");
                 ChatServer.clientList.remove(handler);
             }
         }

         // clean broadcast log
         System.out.println("[Broadcast] " + msg);
     }
 }