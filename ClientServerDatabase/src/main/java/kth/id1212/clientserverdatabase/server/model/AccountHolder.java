/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.model;

import java.rmi.RemoteException;
import kth.id1212.clientserverdatabase.common.Client;

/**
 *
 * @author Jonas
 */
public class AccountHolder {
    
  private final long id;
  private final Client remoteObject;
  private final ClientManager manager;
  private String username;
    
    public AccountHolder(long id, String username, Client remoteObject, ClientManager manager){
        this.id = id;
        this.username = username;
        this.remoteObject = remoteObject;
        this.manager = manager;
    }
    
    public void send(String msg){
        try{
            remoteObject.receive(msg);
        } catch(RemoteException re){
            System.out.println("Failed to send message to remoteObject");
        }
    }
}
