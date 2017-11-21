/*
 * 
 */
package clientserver.client.net;

import java.net.InetSocketAddress;

/**
 * This is an interface for sending messages received from the server to the client without breaking the MVC pattern.
 * @author Jonas
 */
public interface OutHandler {
    
    public void received(String msg);
    public void connected(InetSocketAddress address);
    public void disconnected();
}