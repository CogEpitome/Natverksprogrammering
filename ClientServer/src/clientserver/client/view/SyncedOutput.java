/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserver.client.view;

/**
 *
 * @author Jonas
 * 
 * Provides synchronized console output functions.
 */
class SyncedOutput {
    synchronized void print(String out){
        System.out.print(out);
    }
    synchronized void println(String out){
        System.out.println(out);
    }   
}
