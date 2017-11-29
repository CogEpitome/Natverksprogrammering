/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.client.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import kth.id1212.clientserverdatabase.client.view.ConsoleManager;
import kth.id1212.clientserverdatabase.common.DB;

/**
 *
 * @author Jonas
 */
public class Main {
    public static void main(String[] args){
        try{
            DB db = (DB) Naming.lookup(DB.DB_NAME_IN_REGISTRY);
            new ConsoleManager().start(db);
        } catch (NotBoundException | MalformedURLException | RemoteException e){
            System.out.println("Couldn't start db client");
        }
    }
}
