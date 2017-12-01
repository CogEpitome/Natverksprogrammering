/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kth.id1212.clientserverdatabase.common.Account;
import kth.id1212.clientserverdatabase.common.Client;
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.integration.ServerDAO;
import kth.id1212.clientserverdatabase.server.model.AccountException;
import kth.id1212.clientserverdatabase.server.model.AccountHolder;
import kth.id1212.clientserverdatabase.server.model.ClientManager;
import kth.id1212.clientserverdatabase.server.model.FileDTO;

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
    public synchronized int login(Client remoteObject, Account account){
        if(dao.userExists(account.getUsername())){
            if(dao.credentialsMatch(account.getUsername(), account.getPassword())){
                int clientId = clientManager.createClient(remoteObject, account);
                clientManager.notifyLogin(clientId);
                return clientId;
            } else {
                try{
                    remoteObject.receive("Login failed, incorrect credentials");
                } catch (RemoteException re){
                    System.out.println("Couldn't send login failure notice to remote client");
                }    
            }
        } else {
            try{
                remoteObject.receive("Login failed, no such user exists");
            } catch (RemoteException re){
                System.out.println("Couldn't send login failure notice to remote client");
            }
        }
        return 0;
    }
    
    @Override
    public synchronized void logout(int id){
        clientManager.removeHolder(id);
    }
    
    @Override
    public synchronized int register(Client remoteObject, Account account) throws AccountException{
        try{
            if(!dao.userExists(account.getUsername())){
                dao.register(account);
                return login(remoteObject, account);
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
    
    @Override
    public synchronized List<FileDTO> listFiles(int id) throws SQLException {
        try{
            String username;
            
            if(id != 0){
                AccountHolder user = clientManager.findHolder(id);
                username = user.getUsername();
            } else {
                username = "";
            }
            
            List<FileDTO> files = dao.listFiles();
            List<FileDTO> visibleFiles = new ArrayList<>();
            for(FileDTO file : files){
                if(file.getOwner().equals(username) || file.getAccess().equals("public")){
                    visibleFiles.add(file);
                }
            }
            return visibleFiles;
        } catch (SQLException se){
            throw new RuntimeException("Controller failed listFiles()", se);
        }
    }
    
    @Override
    public synchronized void addFile(FileDTO file) throws AccountException {
        if(!dao.fileExists(file.getName())){
            dao.addFile(file);
        } else {
            throw new AccountException("A file of that name already exists, file name must be unique");
        }
    }
    
    @Override
    public synchronized void removeFile(String filename, int id) throws AccountException {
        if(dao.fileExists(filename)){
            FileDTO file = dao.getFile(filename);
            if(file.getAccess().equals("public")){
                if(file.getPermissions().equals("w")){
                    dao.removeFile(file);
                } else {
                    throw new AccountException("You must be the owner of this file to delete it, because it doesn't have write permission");
                }
            } else {
                AccountHolder user = clientManager.findHolder(id); 
                String username = user.getUsername();
                if(file.getOwner().equals(username)){
                    dao.removeFile(file);
                } else {
                    throw new AccountException("You must be the owner of this file to delete it");
                } 
            }
        } else {
                throw new AccountException("This file does not exist, try enetring a valid filename");
            }
    }
    
    @Override
    public synchronized String download(String filename, int id) throws AccountException{
        if(dao.fileExists(filename)){
            if(id != 0){
                AccountHolder user = clientManager.findHolder(id); 
                String username = user.getUsername();
                notifyChange(dao.getFile(filename).getNotifyId(), filename, username, "downloaded");
                    } else {
                return "You must be logged in to download files";
            }
            return filename+" was found on the server!";
        } else {
            throw new AccountException("This file doesn not exist on the server, please try a different file name");
        }
    }
    
    @Override
    public synchronized void notifyChange(int id, String filename, String username, String action){
        clientManager.notifyFilechange(id, filename, username, action);
        }
    
    @Override
    public synchronized String getUsername(int id){
        return clientManager.findHolder(id).getUsername();
    }
}
