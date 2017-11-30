/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.client.view;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import kth.id1212.clientserverdatabase.client.net.OutHandler;
import kth.id1212.clientserverdatabase.client.net.ServerConnection;
import kth.id1212.clientserverdatabase.common.Account;
import kth.id1212.clientserverdatabase.common.Client;
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.model.AccountException;
import kth.id1212.clientserverdatabase.server.model.FileDTO;

/**
 *
 * @author Jonas
 */
public class ConsoleManager implements Runnable{
    private final SyncedOutput out = new SyncedOutput();
    private final Scanner sc = new Scanner(System.in);
    private DB db;
    private boolean running;
    private int serverId;
    private Client remoteObject;
    
    private final ServerConnection serverConnection = new ServerConnection();
    
    public void start(DB db) throws RemoteException{
        this.db = db;
        this.remoteObject = new ConsoleOutput();
        this.running = true;
        this.serverId = 0;
        new Thread(this).start();
    }
    
    @Override
    public void run(){
        String username, password, filename;
        
        while(running){
            try{
                String in = sc.next();
                switch(in.toUpperCase()){
                    case "SENDFORLOLS":
                        if(serverId != 0){
                            serverConnection.send("testing");
                        }
                        break;
                    
                    case "BUILD":
                        FileDTO file1 = new FileDTO("publicMine.txt", 10, "u", "public", "r", serverId);
                        FileDTO file2 = new FileDTO("privateMine.txt", 10, "u", "private", "r", serverId);
                        FileDTO file3 = new FileDTO("publicWrite.txt", 10, "lolbird", "public", "w", serverId);
                        FileDTO file4 = new FileDTO("privateWrite.txt", 10, "lolbird", "private", "w", serverId);
                        FileDTO file5 = new FileDTO("publicRead.txt", 10, "lolbird", "public", "r", serverId);
                        FileDTO file6 = new FileDTO("privateRead.txt", 10, "lolbird", "private", "r", serverId);
                        try{
                            db.addFile(file1);
                            db.addFile(file2);
                            db.addFile(file3);
                            db.addFile(file4);
                            db.addFile(file5);
                            db.addFile(file6);
                        } catch (AccountException ae){
                            out.println(ae.getMessage());
                        }
                        break;
                    
                    case "LIST":
                        out.println("Listing...");
                        List<FileDTO> files = db.listFiles(serverId);
                        out.println("Returned files of length "+Integer.toString(files.size()));
                        for(FileDTO file : files){
                            out.println("Name: "+file.getName()+" | Size: "+Integer.toString(file.getSize())+" | Owner: "+file.getOwner()+" | Access: "+file.getAccess()+" | Permissions: "+file.getPermissions());
                        }
                        break;
                    
                    case "UPLOAD":
                        FileDTO file = new FileDTO("hi.txt", 10, "lolbird", "public", "rw", serverId);
                        try{
                            db.addFile(file);
                        } catch (AccountException ae){
                            out.println(ae.getMessage());
                        }
                        break;
                        
                    case "DOWNLOAD":
                        out.print("Enter the name of the file you want to download: ");
                        filename = sc.next();
                        try{
                            db.download(filename, serverId);
                        } catch (AccountException ae){
                            out.println(ae.getMessage());
                        }
                        break;
                        
                    case "DELETE":
                        out.print("Enter name of file to delete: ");
                        filename = sc.next();
                        try{
                            db.removeFile(filename, serverId);
                        } catch (AccountException ae){
                            out.println(ae.getMessage());
                        }
                        
                        break;
                        
                    case "LOGIN":
                        if(serverId == 0){
                            
                            try{
                                serverConnection.connect(new Out());
                            } catch (IOException ioe){
                                System.out.println("Failed to connect");
                            }
                            
                            out.println("Enter username");
                            username = sc.next();
                            out.println("Enter password");
                            password = sc.next();
                            serverId = db.login(remoteObject, new Account(username, password));
                        } else {
                            out.println("You are already logged in");
                        }
                        break;
                        
                    case "LOGOUT":
                        if(serverId != 0){
                            running = false;
                            db.logout(serverId);
                            UnicastRemoteObject.unexportObject(remoteObject, false);
                        } else {
                            out.println("You are not logged in");
                        }
                        break;
                        
                    case "REGISTER":
                        if(serverId == 0){
                            out.println("Enter username");
                            username = sc.next();
                            out.println("Enter password");
                            password = sc.next();
                            try{
                                serverId = db.register(remoteObject, new Account(username, password));
                            } catch (AccountException aee){
                                out.println(aee.getMessage());
                            } 
                        } else {
                            out.println("You are already logged in");
                            }
                        break;
                        
                    case "REMOVE":
                        out.println("Enter username");
                        username = sc.next();
                        out.println("Enter password");
                        password = sc.next();
                        try{
                            db.remove(username, password);
                        } catch (AccountException aee){
                            out.println(aee.getMessage());
                        }
                        break;
                    default:
                        out.println("Command " + in + " not recognized");
                        break;
                }
            } catch(RemoteException re){
                out.println("That didn't work, remoteexception");
                out.println(re.getMessage());
            } catch (SQLException se){
                out.println("That didn't work, sqlexception");
                out.println(se.getMessage());
            }
        }
            
    }
    
    private class ConsoleOutput extends UnicastRemoteObject implements Client {
        public ConsoleOutput() throws RemoteException{}
        
        @Override
        public void receive(String msg){
            out.println((String) msg);
        }
    }
    
    private class Out implements OutHandler{
        @Override
        public void handleReceived(String msg){
            out.println(msg);
        }
    }
}
