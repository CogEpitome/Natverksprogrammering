//@author Jonas Iacobi
package clientserver.client.view;

import clientserver.client.controller.Controller;
import clientserver.client.net.OutHandler;
import java.util.*;


//This class receives input from the console using a scanner, and sends it to the server via a Controller object
public class ConsoleManager implements Runnable{
    private final Scanner sc = new Scanner(System.in);
    SyncedOutput out = new SyncedOutput();
    Controller cont;
    private int port;
    private String server;
    boolean active = true;
    
    //Constructor
    public ConsoleManager(Controller cont, String server, int port){ 
        this.cont = cont; this.server = server; this.port = port;
    }

    @Override
    public synchronized void run()
    {
        cont.connect(server, port, new Out());
        //Printed on start
        System.out.println("Bienvenidos a hangman!");
        System.out.println("Guess a letter amigo: ");
        
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
                    cont.send(in);
                }
                
            }
        }
    }
    
    private class Out implements OutHandler {
        @Override
        public void handleReceived(String msg){
            out.println(msg);
            out.println("Guess a letter amigo: ");
        }
    }
    
}
