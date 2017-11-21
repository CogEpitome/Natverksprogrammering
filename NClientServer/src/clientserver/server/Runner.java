
package clientserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

// @author Jonas Iacobi

public class Runner implements Runnable{
    
    private final String PROMPT = "Guess a letter amigo:";
    private Client client;
    private Evaluator evaluator = null;
    private Server server = null;
    PrintWriter out;
    BufferedReader in;
    private boolean connected = true;
    
    
    public Runner(Client client, Server server)
    {
        this.server = server;
        this.client = client;
        this.evaluator = new Evaluator();
    }
    
    //
    @Override
    public void run()
    {
        try{
            out = new PrintWriter(client.socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
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
                    String result = evaluator.evaluate(received, client.session);
                    send(interpret(result));
                }
            }
            catch(IOException ioe)
            {
                try
                {
                    client.socket.close();
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
        
        if(client.session.tries <= 0)
        {
            String oldWord = Arrays.toString(client.session.word);
            client.newWord();
            client.session.tries = client.session.word.length;
            client.session.score--;
            return "You were hung ! " + oldWord + " | Score: "+client.session.score + System.lineSeparator() + System.lineSeparator() + PROMPT;
        }
        else
        if(Arrays.equals(client.session.guessed, client.session.word))
        {
            String oldWord = Arrays.toString(client.session.word);
            client.newWord();
            client.session.tries = client.session.word.length;
            client.session.score++;
            return "You won ! " + oldWord + " | Score: "+client.session.score + System.lineSeparator() + System.lineSeparator() + PROMPT;
        }
        else
        {
            return Arrays.toString(client.session.guessed) + " | "+client.session.tries + " tries remain | Score: " + client.session.score + System.lineSeparator() + PROMPT;
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
