/*
 * @author Jonas Iacobi
 */
package kth.id1212.clientserverdatabase.server.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//This is the main server class, it accepts messages from clients
public class FileServer implements Runnable{
    private static final int LINGERTIME = 10000;
    private static final int PORT = 9080;
    
    private int port;
    private boolean running = true;
    private ServerSocket socket;
    private Socket clientSocket;
    private Thread runner;

    //Constructor
    public FileServer(int port)
    {
        this.port = port;
    }
    
    public void run(){
        //Open a server socket on a given port
        try
        {
            this.socket = new ServerSocket(this.port);
        }
        catch(IOException ioe)
        {
            throw new RuntimeException("Couldn't access port "+port);
        }
        
        //Main loop
        while(running())
        {
            try //Attempt to connect to a client
            {
                clientSocket = this.socket.accept();
                clientSocket.setSoLinger(true, LINGERTIME);
            }
            catch(IOException ioe)
            {
                throw new RuntimeException("Couldn't accept client");
            }
            
            //Create a Runner class to handle the client's request, then continue listening
            Thread thisRunner = new Thread(new Runner(clientSocket, this));
            thisRunner.setPriority(Thread.MAX_PRIORITY);
            thisRunner.start();
            
        }
        System.out.println("Server terminated");
    }
    
    
    
    //Check if the server is running
    private synchronized boolean running()
    {
        return this.running;
    }
    
    //Stop the server
    public synchronized void stop()
    {
        this.running = false;
        try
        {
            this.socket.close();
        }
        catch(IOException ioe)
        {
            throw new RuntimeException("Couldn't close server. Y'all screwed.");
        }
    }
}
