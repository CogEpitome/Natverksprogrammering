//@author Jonas Iacobi

package clientserver.startup;

import clientserver.client.net.ServerConnection;
import clientserver.client.view.ConsoleManager;

//Starts a client
public class Main {
    //Set the port and server address the client will try to connect to
    private static final int PORT = 9080;
    private static final String SERVER = "localhost";
    
    //Create and start the client
    public static void main(String[] args)
    {
        ServerConnection serCon = new ServerConnection();
        ConsoleManager conMan = new ConsoleManager(serCon, SERVER, PORT);
        
        new Thread(conMan).start();
    }
}
