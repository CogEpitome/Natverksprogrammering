/*
 * @author Jonas Iacobi
 */
package clientserver.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

//This is the main server class, it accepts messages from clients
public class Server{
    private static final int LINGERTIME = 10000;
    private static final int PORT = 9080;
    
    private int port;
    private boolean running = true;
    private ServerSocket socket = null;
    private Socket client = null;
    private Thread runner = null;
    private FileHandler fileHandler = null;
    protected Session session = null;
    
    //Session keeps track of a game session's variables
    protected class Session
    {
        protected char[] word;
        protected char[] guessed;
        protected int tries;
        protected int score;
        
        protected Session get(Socket client){
            return this;
        }
    }
    
    
     public static void main(String[] args){
         Server server = new Server(PORT);
         server.start();
        }

    //Constructor
    public Server(int port)
    {
        this.port = port;
        this.fileHandler = new FileHandler();
        this.session = new Session();
    }
    

    public void start()
    {
        //Initialize on start. Sets the first word of the game.
        setWord(fileHandler.getWord(), session);
        session.tries = session.word.length;
        
        serve();
        
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
                client = this.socket.accept();
                client.setSoLinger(true, LINGERTIME);
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
    
    //Updates the session with a new word from the FileHandler class
    protected void newWord()
    {
        setWord(fileHandler.getWord(), session);
        session.tries = session.word.length;
    }
    
    //Sets the session's word and resets the session's guessed array
    private void setWord(char[] word, Session session)
    {
        session.word = word;
        session.guessed = new char[word.length];
        for(int i = 0; i < session.guessed.length; i++) { session.guessed[i] = '_'; }
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
