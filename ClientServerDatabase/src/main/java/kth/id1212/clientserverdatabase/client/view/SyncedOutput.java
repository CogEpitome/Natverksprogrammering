/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.client.view;

/**
 *
 * @author Jonas
 */
public class SyncedOutput {
    public synchronized void println(String string){
        System.out.println(string);
    }
    
    public synchronized void print(String string){
        System.out.print(string);
    }
}
