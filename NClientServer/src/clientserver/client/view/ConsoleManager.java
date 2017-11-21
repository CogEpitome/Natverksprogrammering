//@author Jonas Iacobi
package clientserver.client.view;

import clientserver.client.net.*;
import java.net.InetSocketAddress;
import java.util.*;


//This class receives input from the console using a scanner, and sends it to the server via a Controller and ServerConnection
public class ConsoleManager implements Runnable{
    private final Scanner sc = new Scanner(System.in);
    SyncedOutput out = new SyncedOutput();
    ServerConnection connection;
    private final int port;
    private final String server;
    boolean active = true;
    
    //Constructor
    public ConsoleManager(ServerConnection connection, String server, int port){ 
        this.connection = connection; this.server = server; this.port = port;
    }

    @Override
    public synchronized void run()
    {
        connection.connect(server, port, new Out());
        //Printed on start
        System.out.println("Bienvenidos a hangman!");
        
        //The main client side loop
        while(active)
        {
            String in = sc.next();
            {
                //"." stops the client side.
                if(in.equals(".")) 
                {
                    System.out.println("Game ended");
                    active = false;
                }
                //Send the user's guess to the server
                else
                {
                    connection.qSend(in);
                }
                
            }
        }
    }
    
    //Prints the server's reply
    private class Out implements OutHandler {
        @Override
        public void received(String msg){
            out.println(msg);
        }
    }
    
}
