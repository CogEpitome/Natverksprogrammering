//@author Jonas Iacobi

package clientserver.client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//This is the class which communicates with the server and prints to console.
public class Controller implements Runnable{
    
    private final int timeOut = 10000;
    private int PORT = 9080;
    private final String SERVER = "localhost";
    private String msg;
    
    //Constructor
    public Controller(){}
    
    //The public method called when sending a message to the server
    @Override
    public void run()
    {
        System.out.println(connect(SERVER, PORT, msg));
        System.out.print("Guess a letter amigo: ");
    }
    
    //Connects to the server, sends a message, and returns the reply
    private String connect(String server, int port, String msg)
    {
     try(Socket socket = new Socket(server, port))
     {
        socket.setSoTimeout(timeOut);
        send(msg, socket);
        return read(socket);
     }  
     catch(IOException ioe)
     {
         throw new RuntimeException("Failed to establish connection to server", ioe);
     }
    }
    
    //This method sends the message to the server socket's stream
    private void send(String msg, Socket socket) throws IOException
    {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(msg);
        out.println();
        out.flush();
    }
    
    //Reads the server's reply
    private String read(Socket socket) throws IOException
    {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String reply = in.readLine();
        return reply;
    }
    
    //Setter for the message to be sent to the server
    public void setMessage(String msg)
    {
        this.msg = msg;
    }
}
