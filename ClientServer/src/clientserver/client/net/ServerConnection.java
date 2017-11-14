/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserver.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
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
    
    public void connect(String server, int port, OutHandler outHandler) throws IOException{
        socket = new Socket();
        socket.connect(new InetSocketAddress(server, port), 10000);
        socket.setSoTimeout(1000000);
        connected = true;
        
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        new Thread(new Ihearyou(outHandler)).start();
    }
    
    public void disconnect() throws IOException{
        send("DISCONNECT");
        socket.close();
        socket = null;
        connected = false;
    }
    
    public void send(String msg){
        out.println(msg);
    }
    
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
