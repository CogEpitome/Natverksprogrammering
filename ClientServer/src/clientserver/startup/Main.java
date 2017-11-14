//@author Jonas Iacobi

package clientserver.startup;

import clientserver.server.Server;
import clientserver.client.controller.Controller;
import clientserver.client.view.ConsoleManager;

//Start the server and start the client
public class Main {
    //Set the port the client and server will use
    private static final int PORT = 9080;
    private static final String SERVER = "localhost";
    
    //Create and start the server and client
    public static void main(String[] args)
    {
        //Server server = new Server(PORT);
        Controller cont = new Controller();
        ConsoleManager conMan = new ConsoleManager(cont, SERVER, PORT);
        
        //new Thread(server).start();
        new Thread(conMan).start();
    }
}
