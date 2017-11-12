
package clientserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @author Jonas Iacobi

public class Runner implements Runnable{
    
    private Socket client = null;
    private Server.Session session = null; 
    private Evaluator evaluator = null;
    private Server server = null;
    
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
            throw new RuntimeException("Failed to read the http request from client.");
        }
        finally
        {
            try
            {
                client.close();
            }
            catch(IOException ioe)
            {
                throw new RuntimeException("Failed to close client connection.");
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
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String line = in.readLine();
        return line;
    }
    
    private void send(String send) throws IOException
    {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println(send);
        out.flush();      
    }
}
