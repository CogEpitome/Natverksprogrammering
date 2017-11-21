package clientserver.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author Jonas
 */
public class ServerConnection implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean connected;
    private InetSocketAddress address;
    private SocketChannel channel;
    private Selector selector;
    private final List<OutHandler> listeners = new ArrayList<>();
    
    public void run(){
        try{
            initialize();
            
            while(connected){
                //select will return once a conncetion is established,
                //preventing the rest of the while loop from blocking.
                selector.select();
                for(SelectionKey key : selector.selectedKeys()){
                    selector.selectedKeys().remove(key);
                    if(key.isValid()){
                        if(key.isConnectable()){
                            finalizeConnection(key);
                        } else 
                        if(key.isReadable()){
                            received(key);
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
        
        //After connection is set false
            
        
    }
    
    public void connect(String server, int port){
        this.address = new InetSocketAddress(server, port);
        new Thread(this).start();
    }
    
    private void finalizeConnection(SelectionKey key) throws IOException{
        channel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
        try{
            informDone((InetSocketAddress)channel.getRemoteAddress());
        } catch (IOException ioe){
            throw new RuntimeException("Failed to get remote address");
        }
    }
    
    private void informDone(InetSocketAddress add){
        Executor pool = ForkJoinPool.commonPool();
        listeners.forEach((listener) -> {
            pool.execute(() -> {
                listener.connected(add);
            });
        });
    }
    
    public void addListener(OutHandler listener){
        listeners.add(listener);
    }
    
    private void initialize() throws IOException{
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(address);
        connected = true;
        
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
    }
    

    
}
