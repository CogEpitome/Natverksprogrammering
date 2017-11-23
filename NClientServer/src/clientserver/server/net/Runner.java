
package clientserver.server.net;

import clientserver.server.net.Server;
import clientserver.server.model.Evaluator;
import clientserver.server.model.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;

// @author Jonas Iacobi

public class Runner implements Runnable{
    
    private final String PROMPT = "Guess a letter amigo:";
    private final Client client;
    
    
    private Evaluator evaluator = null;
    
    PrintWriter out;
    BufferedReader in;
    private boolean connected = true;
    private String receivedStr;
    
    private Server server = null;
    private SocketChannel clientChannel;
    private final ByteBuffer received = ByteBuffer.allocateDirect(100);
    private Server.ServerClient serverClient;
    
    private final Queue<ByteBuffer> sendQ = new ArrayDeque<>();
    
    public Runner(Server server)
    {
        this.server = server;
        this.evaluator = new Evaluator();
        this.client = new Client();
    }
    
    //
    @Override
    public void run()
    {
        String result = interpret(receivedStr);
        sendQ.add(ByteBuffer.wrap(result.getBytes()));
        server.send(result, clientChannel);
    }

    public void send() throws IOException
    {
        ByteBuffer msg;
        synchronized(sendQ){
                while((msg = sendQ.peek()) != null){
                    clientChannel.write(msg);
        if(msg.hasRemaining()){
            throw new IOException("Failed to send message");
        }
                    System.out.println("ServerClient calling runner.send");
                    sendQ.remove();
                }
            }
        
        
    }
    
    public void receive() throws IOException
    {
        received.clear();
        int readBytes;
        readBytes = clientChannel.read(received);
        if(readBytes == -1){
            throw new IOException("No connection to client");
        }
        received.flip();
        byte[] bytes = new byte[received.remaining()];
        received.get(bytes);
        receivedStr = new String(bytes);
        
        ForkJoinPool.commonPool().execute(this);//Interpreting thread, to not interfere with communicating thread
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
}
