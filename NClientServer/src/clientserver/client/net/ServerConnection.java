package clientserver.client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author Jonas
 */
public class ServerConnection implements Runnable{
    private volatile boolean connected;
    private InetSocketAddress address;
    private SocketChannel channel;
    private Selector selector;
    private final List<OutHandler> listeners = new ArrayList<>();
    private final ByteBuffer received = ByteBuffer.allocateDirect(100);
    private final Queue<ByteBuffer> sendQ = new ArrayDeque();
    private boolean sending = false;
    
    @Override
    public void run(){
        try{
            initialize();
            
            while(connected || !sendQ.isEmpty()){
                //Inform the selector that we'd like to send a message
                if(sending){
                    channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    sending = false;
                }
                //select will return once a conncetion is established,
                //preventing the rest of the while loop from blocking.
                selector.select();
                for(SelectionKey key : selector.selectedKeys()){
                    selector.selectedKeys().remove(key);
                    if(key.isValid()){
                        if(key.isConnectable()){
                            channel.finishConnect();
                            key.interestOps(SelectionKey.OP_READ);
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
    
    public void connect(String server, int port, OutHandler listener){
        this.address = new InetSocketAddress(server, port);
        listeners.add(listener);
        new Thread(this).start();
    }
    
    //This interface method is passed to the view and results in I/O, so it is executed using the built in thread pool to prevent delays.
    private void informReceived(String str){
        Executor pool = ForkJoinPool.commonPool();
        listeners.forEach((listener) -> {
            pool.execute(() -> {
                listener.received(str);
            });
        });
    }
    
    public void qSend(String msg){
        sendQ.add(ByteBuffer.wrap(msg.getBytes()));
        sending = true;
        selector.wakeup();
    }
    
    private void send(SelectionKey key) throws IOException{
        ByteBuffer msg;
        synchronized(sendQ){
            while((msg = sendQ.peek()) != null){
                channel.write(msg);
                if(msg.hasRemaining()) return; //In case the entire buffer wasn't sent
                sendQ.remove();
            }
            key.interestOps(SelectionKey.OP_READ);
        }
    }
    
    private void receive(SelectionKey key) throws IOException{
        received.clear();
        int length = channel.read(received);
        if(length == -1){
            throw new IOException("Server reply empty");
        }
        
        received.flip();
        byte[] receivedBytes = new byte[received.remaining()];
        received.get(receivedBytes);
        String receivedStr = new String(receivedBytes);
        informReceived(receivedStr);
        
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
