/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.common;

import java.io.Serializable;
/**
 *
 * @author Jonas
 */
public class Account implements Serializable {
    private final String username;
    private final String password;
    
    public Account(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getUsername(){
        return username;
    }
}
