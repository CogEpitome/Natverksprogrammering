
package kth.id1212.clientserverdatabase.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

// @author Jonas Iacobi

public class Runner implements Runnable{
    
    private final String PROMPT = "Guess a letter amigo:";
    private FileServer server = null;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    private boolean connected = true;
    
    
    public Runner(Socket clientSocket, FileServer server)
    {
        this.server = server;
        this.clientSocket = clientSocket;
    }
    
    //
    @Override
    public void run()
    {
        try{
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch(IOException ioe){
            throw new RuntimeException(ioe);
        }
        
        while(connected)
            {
            try
            {
                String received = read();
                if(received != null)
                {
                    send("This is a server reply to "+received);
                }
            }
            catch(IOException ioe)
            {
                try
                {
                    clientSocket.close();
                    connected = false;
                } catch(IOException ioe2) {
                    throw new RuntimeException("Failed to close client connection.");
                }
                throw new RuntimeException("Failed to read the request from client.");
            }
            }

    }
    
    private String read() throws IOException
    {
        
        String line = in.readLine();
        return line;
    }
    
    private void send(String send) throws IOException
    {
        
        out.println(send);
        out.flush();      
    }
}
