/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase.server.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Jonas
 */
public class FileDTO implements Serializable {
    private final String filename, owner, access, permissions;
    private final int size, notifyId;
    
    public FileDTO(String filename, int size, String owner, String access, String permissions, int notifyId){
        this.filename = filename;
        this.size = size;
        this.owner = owner;
        this.access = access;
        this.permissions = permissions;
        this.notifyId = notifyId;
    }
    
    public String getName(){
        return this.filename;
    }
    public int getSize(){
        return this.size;
    }
    public String getOwner(){
        return this.owner;
    }
    public String getAccess(){
        return this.access;
    }
    public String getPermissions(){
        return this.permissions;
    }
    public int getNotifyId(){
        return this.notifyId;
    }
}
