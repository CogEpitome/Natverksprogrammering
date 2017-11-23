/*
 * @author Jonas Iacobi
 */
package clientserver.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

//This is the main server class, it accepts messages from clients
public class Server{
    private static final int LINGERTIME = 10000;
    private static final int PORT = 9080;
    
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private boolean sending = false;
    
    public static void main(String[] args){
        Server server = new Server();
        server.start();
        }

    public Server(){}
    
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
    
    public void notifySend(ByteBuffer message){
        sending = true;
        selector.wakeup();
    }
     
    public void serve(){
        //Main loop
        try{
            while(true)
            {
                //Notify the keys we're interested in writing something
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

    private void startHandler(SelectionKey key) throws IOException{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept(); //Establish connection
        clientChannel.configureBlocking(false);
        Runner runner = new Runner(this, clientChannel);
        clientChannel.register(selector, SelectionKey.OP_READ, new ServerClient(runner));
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGERTIME);
    }
    
    private void receive(SelectionKey key) throws IOException{
        ServerClient serverClient = (ServerClient) key.attachment();
        try{
            serverClient.runner.receive();
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
        selector.keys().stream().filter((key) -> (key.channel() instanceof SocketChannel && key.isValid())).forEachOrdered((key) -> {
            key.interestOps(SelectionKey.OP_WRITE);
        });
    }
    
    //This class connects a key to a client, serving as the link between the server and individual client communication channels.
    protected class ServerClient{
        private final Runner runner;
        
        private ServerClient(Runner runner){
            this.runner = runner;
        }
        
        private void send() throws IOException {
            runner.send();     
        }
    }

}
