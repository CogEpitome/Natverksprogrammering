//@author Jonas Iacobi

package clientserver.client.controller;

import clientserver.client.net.OutHandler;
import clientserver.client.net.ServerConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

//This is the class which communicates with the server and prints to console.
public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();
    
    //Constructor
    public Controller(){}
    
    //The public method called when sending a message to the server
    public void connect(String server, int port, OutHandler outHandler)
    {
        CompletableFuture.runAsync(() -> {
            try{
                serverConnection.connect(server, port, outHandler);
            } catch (IOException ioe){
             throw new RuntimeException("Failed to establish connection to server", ioe);  
            }
        });

        System.out.print("Guess a letter amigo: ");
    }
    
    public void disconnect()
    {
        try{
        serverConnection.disconnect();
        } catch(IOException ioe){
            throw new RuntimeException("Disconnect failed", ioe);
        }
    }
    
    public void send(String msg)
    {
        CompletableFuture.runAsync(() -> serverConnection.send(msg));
    }

}
