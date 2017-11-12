//@author Jonas Iacobi
package clientserver.client.view;

import clientserver.client.controller.Controller;
import java.util.*;
import java.io.*;


//This class receives input from the console using a scanner, and sends it to the server via a Controller object
public class ConsoleManager implements Runnable{
    private final Scanner sc = new Scanner(System.in);
    Controller cont;
    boolean active = true;
    
    //Constructor
    public ConsoleManager(Controller cont){ this.cont = cont; }
    
    @Override
    public synchronized void run()
    {
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
                //Send the user's guess to the server on a separate thread
                else
                {
                    cont.setMessage(in);
                    new Thread(cont).start();
                }
                
            }
        }
    }
    
}
