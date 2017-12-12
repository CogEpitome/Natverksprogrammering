/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jonas
 */
public class Currency {
    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> tlem = new ThreadLocal<>();
    
    public Currency(){
        emFactory = Persistence.createEntityManagerFactory("CurrencyPU");
    }
}
