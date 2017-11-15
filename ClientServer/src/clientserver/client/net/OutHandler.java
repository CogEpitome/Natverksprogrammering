/*
 * 
 */
package clientserver.client.net;

/**
 * This is an interface for sending messages received from the server to the client without breaking the MVC pattern.
 * @author Jonas
 */
public interface OutHandler {
    
    public void handleReceived(String msg);
    
}
