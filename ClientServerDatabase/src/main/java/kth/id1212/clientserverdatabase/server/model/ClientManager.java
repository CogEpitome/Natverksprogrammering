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
    private final Map<Long, AccountHolder> holders = Collections.synchronizedMap(new HashMap<>());
    
    public long createClient(Client remoteObject, Account account){
        long clientId = idGen.nextLong();
        AccountHolder holder = new AccountHolder(clientId, account.getUsername(), remoteObject, this);
        holders.put(clientId, holder);
        return clientId;
    }
    
    public AccountHolder findHolder(long id){
        return holders.get(id);
    }
    
    public void removeHolder(long id){
        holders.remove(id);
    }
    
    public void notifyLogin(long id){
        findHolder(id).send("Successfully logged in!");
    }
}
