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

/**
 *
 * @author Jonas
 */
public interface DB extends Remote{
    public static final String DB_NAME_IN_REGISTRY = "database_hw3";
    
    //Methods for manipulating db
    public List<String> listUsers() throws RemoteException, SQLException;
}
