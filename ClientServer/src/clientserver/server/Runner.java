
package clientserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

// @author Jonas Iacobi

public class Runner implements Runnable{
    
    private Socket client = null;
    private Server.Session session = null; 
    private Evaluator evaluator = null;
    private Server server = null;
    PrintWriter out;
    BufferedReader in;
    private boolean connected = true;
    
    
    public Runner(Socket client, Server server)
    {
        this.server = server;
        this.client = client;
        this.session = server.session;
        this.evaluator = new Evaluator();
    }
    
    //
    @Override
    public void run()
    {
        try{
            out = new PrintWriter(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
                    String result = evaluator.evaluate(received, session);
                    send(interpret(result));
                }
            }
            catch(IOException ioe)
            {
                try
                {
                    client.close();
                    connected = false;
                } catch(IOException ioe2) {
                    throw new RuntimeException("Failed to close client connection.");
                }
                throw new RuntimeException("Failed to read the request from client.");
            }
            }

    }
    
    private String interpret(String result)
    {
        
        if(session.tries <= 0)
        {
            String oldWord = Arrays.toString(server.session.word);
            server.newWord();
            server.session.tries = server.session.word.length;
            server.session.score--;
            return "You were hung ! " + oldWord + " | Score: "+session.score + System.lineSeparator() + System.lineSeparator();
        }
        else
        if(Arrays.equals(session.guessed, session.word))
        {
            String oldWord = Arrays.toString(server.session.word);
            server.newWord();
            server.session.tries = server.session.word.length;
            server.session.score++;
            return "You won ! " + oldWord + " | Score: "+session.score + System.lineSeparator() + "Press any key to continue"  + System.lineSeparator();
        }
        else
        {
            return Arrays.toString(server.session.guessed) + " | "+server.session.tries + " tries remain | Score: " + server.session.score;
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
