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
import kth.id1212.clientserverdatabase.common.DB;
import kth.id1212.clientserverdatabase.server.integration.ServerDAO;

/**
 *
 * @author Jonas
 */
public class Controller extends UnicastRemoteObject implements DB {
    private final ServerDAO dao;
    
    public Controller(String dbtype, String source) throws RemoteException {
        super();
        dao = new ServerDAO(dbtype, source);
       
    }
    
    //DB manipulation methods overrides
    @Override
    public synchronized List<String> listUsers() throws SQLException {
        try{
            return dao.listUsers();
        } catch (SQLException se){
            throw new RuntimeException("Controller failed listUsers()", se);
        }
    }
}
