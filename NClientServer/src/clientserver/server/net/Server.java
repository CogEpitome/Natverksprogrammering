/*
 * @author Jonas Iacobi
 */
package clientserver.server.net;

import clientserver.server.model.Client;
import clientserver.server.filehandler.FileHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

//This is the main server class, it accepts messages from clients
public class Server{
    private static final int LINGERTIME = 10000;
    private static final int PORT = 9080;
    
    private final int port;
    private boolean running = true;
    
    private Client client;
    private final List<Client> clients;
    
    private final Queue<ByteBuffer> sendQ = new ArrayDeque();
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private boolean sending = false;
    
    public static void main(String[] args){
        Server server = new Server(PORT);
        server.start();
        }

    //Constructor
    public Server(int port)
    {
        this.port = port;
        this.clients = new ArrayList<>();
    }
    
    public void start(){
        try{
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.configureBlocking(false);
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);  
        } catch (IOException ioe){
            throw new RuntimeException(ioe);
        }
        
        serve();
    }
    
    public void sendToClient(ByteBuffer message, ServerClient serverClient){
        sending = true;
        synchronized(serverClient.sendQ){
            serverClient.Qsend(message);
        }
        selector.wakeup();
    }
     
    public void serve(){
        //Main loop
        try{
            while(true)
            {
                if(sending){
                    interestWrite();
                    sending  = false;
                }
                //Ensures we're not blocking
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isValid()){
                        if(key.isAcceptable()){
                            startHandler(key);
                        } else
                        if(key.isReadable()){
                            receive(key);
                        } else
                        if(key.isWritable()){
                            send(key);
                        }
                    }
                }
            }
        } catch(IOException ioe){
            throw new RuntimeException(ioe);
        }
    }
    /**/
    private void startHandler(SelectionKey key) throws IOException{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept(); //Establish connection
        clientChannel.configureBlocking(false);
        Runner runner = new Runner(this, clientChannel);
        clientChannel.register(selector, SelectionKey.OP_READ, new ServerClient(runner));
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, 100000);
    }
    
    private void receive(SelectionKey key) throws IOException{
        ServerClient serverClient = (ServerClient) key.attachment();
        try{
            serverClient.runner.receive(serverClient);
        } catch (IOException ioe){
            throw new IOException("Couldn't receive from serverClient's runner", ioe);
        }
    }
    
    private void send(SelectionKey key) throws IOException{
        ServerClient serverClient = (ServerClient) key.attachment();
        try{
            serverClient.send();
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException ioe){
            throw new IOException("error sending", ioe);
        }
    }
    
    private void interestWrite() {
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel && key.isValid()) {
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }
    }
    
    
    protected class ServerClient{
        private final Runner runner;
        public final Queue<ByteBuffer> sendQ = new ArrayDeque<>();
        
        private ServerClient(Runner runner){
            this.runner = runner;
        }
        
        public void Qsend(ByteBuffer msg){
            synchronized(sendQ){
                sendQ.add(msg.duplicate());
            }
        }
        
        private void send() throws IOException {
            ByteBuffer msg = null;
            synchronized(sendQ){
                while((msg = sendQ.peek()) != null){
                    runner.send(msg);
                    sendQ.remove();
                }
            }            
        }
    }
    
    private void appendClientQueue(ServerClient serverClient) {

        synchronized (sendQ) {

            ByteBuffer msgToSend;

            while ((msgToSend = sendQ.poll()) != null) {


                    synchronized (serverClient.sendQ) {

                        serverClient.Qsend(msgToSend);



                    }

                }

            }

        }

    
    
    /*private Client addClient(Socket clientSocket){
        Client newClient = new Client(clientSocket);
        clients.add(client);
        System.out.println("New client added");
        return newClient;
    }*/
}
