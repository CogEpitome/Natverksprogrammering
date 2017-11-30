package kth.id1212.clientserverdatabase.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Jonas
 */
public class ServerConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean connected;
    
    private final String SERVER = "localhost";
    private final int PORT = 9080;
    
    //This method is unsed to connect to the server
    public void connect(OutHandler outHandler) throws IOException{
        socket = new Socket();
        socket.connect(new InetSocketAddress(SERVER, PORT), 10000);
        socket.setSoTimeout(1000000);
        connected = true;
        
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        new Thread(new Ihearyou(outHandler)).start();
    }
    
    //Disconnects the client from the server and closes the socket.
    public void disconnect() throws IOException{
        socket.close();
        socket = null;
        connected = false;
    }
    
    //Sends a message to the server
    public void send(String msg){
        out.println(msg);
    }
    
    //This class is responsible for listening for server replies.
    private class Ihearyou implements Runnable 
    {
        private final OutHandler outHandler;
        
        private Ihearyou(OutHandler outHandler){
            this.outHandler = outHandler;
        }
        
        @Override
        public void run()
        {
            try{
                for(;;){
                    outHandler.handleReceived(in.readLine());
                }
            } catch (IOException ioe) {
                if(connected)
                    throw new RuntimeException("No connection", ioe);
            }
        }
    }
}
