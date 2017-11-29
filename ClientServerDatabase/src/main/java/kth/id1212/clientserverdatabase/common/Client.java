/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jonas
 */
public interface Client extends Remote{
    void receive(String msg) throws RemoteException;
}
