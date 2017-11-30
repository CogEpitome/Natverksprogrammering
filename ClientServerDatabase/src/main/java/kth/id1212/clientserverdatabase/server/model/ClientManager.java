/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import kth.id1212.clientserverdatabase.common.Account;
import kth.id1212.clientserverdatabase.common.Client;

/**
 *
 * @author Jonas
 */
public class ClientManager {
    private final Random idGen = new Random();
    private final Map<Integer, AccountHolder> holders = Collections.synchronizedMap(new HashMap<>());
    
    public int createClient(Client remoteObject, Account account){
        int clientId = idGen.nextInt();
        AccountHolder holder = new AccountHolder(clientId, account.getUsername(), remoteObject, this);
        holders.put(clientId, holder);
        return clientId;
    }
    
    public AccountHolder findHolder(int id){
        return holders.get(id);
    }
    
    public void removeHolder(int id){
        holders.remove(id);
    }
    
    public void notifyLogin(int id){
        findHolder(id).send("Successfully logged in!");
    }
    
    public void notifyFilechange(int id, String filename, String username, String action){
        AccountHolder holder = findHolder(id);
        if(holder != null){
            holder.send(username+" "+action+" your file "+filename+"!");
        }
    }
}
