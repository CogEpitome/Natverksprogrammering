/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.controller.Controller;
import kth.id1212.clientserverdatabase.server.net.FileServer;

/**
 *
 * @author Jonas
 */
public class Server {
    
    private final String dbName = DB.DB_NAME_IN_REGISTRY;
    private final String dbManagementSystem = "derby";
    private final String source = "HW4DB";
    
    private static final int PORT = 9080;
    
    public static void main(String[] args){
        try{
            Server server = new Server();
            server.startServant();
            new Thread(new FileServer(PORT)).start();
            System.out.println("Server started successfully!");
        } catch(RemoteException | MalformedURLException e){
            System.out.println("Server failed to start :<");
        }
    }
    
    private void startServant() throws RemoteException, MalformedURLException {
        try{
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noReg){
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller(dbManagementSystem, source);
        Naming.rebind(dbName, controller);
    }
}
