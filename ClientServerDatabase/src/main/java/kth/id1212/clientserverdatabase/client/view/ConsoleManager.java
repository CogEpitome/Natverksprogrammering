/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.client.view;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.model.AccountException;

/**
 *
 * @author Jonas
 */
public class ConsoleManager implements Runnable{
    private final SyncedOutput out = new SyncedOutput();
    private final Scanner sc = new Scanner(System.in);
    private DB db;
    private boolean running;
    
    public void start(DB db){
        this.db = db;
        running = true;
        new Thread(this).start();
    }
    
    @Override
    public void run(){
        String username, password;
        while(running){
            try{
                String in = sc.next();
                switch(in.toUpperCase()){
                    case "LIST":
                        List<String> users = db.listUsers();
                        for(String user : users){
                            out.println(user);
                        }
                        break;
                        
                    case "REGISTER":
                        out.println("Enter username");
                        username = sc.next();
                        out.println("Enter password");
                        password = sc.next();
                        try{
                            db.register(username, password);
                        } catch (AccountException aee){
                            out.println(aee.getMessage());
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
}
