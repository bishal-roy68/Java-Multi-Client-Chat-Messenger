/*
* Final day updated the code from Bishal
*  Reference: used partial  code idea from textbook...
* */

package server;


import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ChatServer {

    // list of all connected clients
    public static ArrayList<ClientHandler> clientList = new ArrayList<>();

    // counting clients for IDs
    public static int clientCount = 0;

    // just making time format for logs
    static String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("=====================================");
            System.out.println("Server is ON!! (Port " + port + ") - " + getTime());
            System.out.println("=====================================");

            // this will keep the server running forever and accept every new client
            while (true) {

                // when a new client connects
                Socket theClient = server.accept();
                clientCount++;

                // simple join log
                System.out.println("[" + getTime() + "] New client (" + clientCount + ") connecting...");

                //making a handler for this client
                ClientHandler handler = new ClientHandler(theClient, clientCount);

                // save to list
                clientList.add(handler);

                // starting thread for this client
                handler.start();
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Something went wrong on server.");
        }
    }
}
