/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import kth.id1212.clientserverdatabase.server.model.AccountException;
import kth.id1212.clientserverdatabase.server.model.FileDTO;

/**
 *
 * @author Jonas
 */
public interface DB extends Remote{
    public static final String DB_NAME_IN_REGISTRY = "database_hw3";
    
    //Methods for manipulating db
    int register(Client remoteObject, Account account) throws RemoteException, AccountException;
    
    int login(Client remoteObject, Account account) throws RemoteException;
    
    void logout(int id) throws RemoteException;
    
    public void remove(String username, String password) throws RemoteException, AccountException;
    
    public List<String> listUsers() throws RemoteException, SQLException;
    
    public List<FileDTO> listFiles(int id) throws RemoteException, SQLException;
    
    public void addFile(FileDTO file) throws RemoteException, AccountException;
    
    public void removeFile(String filename, int id) throws RemoteException, AccountException;
    
    public String download(String filename, int id) throws RemoteException, AccountException;
    
    public void notifyChange(int id, String filename, String username, String action) throws RemoteException;
    
    public String getUsername(int id) throws RemoteException;
}
