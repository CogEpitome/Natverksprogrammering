/*
 * @author Jonas Iacobi
 */
package clientserver.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//This is the main server class, it accepts messages from clients
public class Server{
    private static final int LINGERTIME = 10000;
    private static final int PORT = 9080;
    
    private int port;
    private boolean running = true;
    private ServerSocket socket;
    private Socket clientSocket;
    private Client client;
    private List<Client> clients;
    private Thread runner;
    private FileHandler fileHandler;
    
    
     public static void main(String[] args){
         Server server = new Server(PORT);
         server.start();
        }

    //Constructor
    public Server(int port)
    {
        this.port = port;
        this.fileHandler = new FileHandler();
        this.clients = new ArrayList<>();
    }
    

    public void start()
    {
        serve();      
    }
    
    private Client addClient(Socket clientSocket){
        Client newClient = new Client();
        newClient.socket = clientSocket;
        clients.add(client);
        System.out.println("New client added");
        return newClient;
    }
    
    public void serve(){
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
                client = addClient(clientSocket);
            }
            catch(IOException ioe)
            {
                throw new RuntimeException("Couldn't accept client");
            }
            
            //Create a Runner class to handle the client's request, then continue listening
            Thread thisRunner = new Thread(new Runner(client, this));
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
