/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import kth.id1212.clientserverdatabase.common.Account;
import kth.id1212.clientserverdatabase.common.Client;
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.integration.ServerDAO;
import kth.id1212.clientserverdatabase.server.model.AccountException;
import kth.id1212.clientserverdatabase.server.model.ClientManager;

/**
 *
 * @author Jonas
 */
public class Controller extends UnicastRemoteObject implements DB {
    private final ServerDAO dao;
    private final ClientManager clientManager = new ClientManager();
    
    public Controller(String dbtype, String source) throws RemoteException {
        super();
        dao = new ServerDAO(dbtype, source);
       
    }
    
    @Override
    public synchronized long login(Client remoteObject, Account account){
        long clientId = clientManager.createClient(remoteObject, account);
        clientManager.notifyLogin(clientId);
        return clientId;
    }
    
    @Override
    public synchronized void logout(long id){
        clientManager.removeHolder(id);
    }
    
    @Override
    public synchronized void register(String username, String password) throws AccountException{
        try{
            if(!dao.userExists(username)){
                dao.register(username, password);
            } else {
                throw new AccountException("An account with this username already exists!");
            }
        } catch(AccountException aee){
            throw new AccountException("An account with this username already exists!");
        }
    }
    
    @Override
    public synchronized void remove(String username, String password) throws AccountException{
        try{
            if(dao.userExists(username)){
                if(dao.credentialsMatch(username, password)){
                    dao.remove(username);
                } else {
                    throw new AccountException("Incorrect credentials");
                }
            } else {
                throw new AccountException("No such account exists!");
            }
        } catch(AccountException aee){
            throw new AccountException("Something went wrong");
        }
    }
    
    
    @Override
    public synchronized List<String> listUsers() throws SQLException {
        try{
            return dao.listUsers();
        } catch (SQLException se){
            throw new RuntimeException("Controller failed listUsers()", se);
        }
    }
}
