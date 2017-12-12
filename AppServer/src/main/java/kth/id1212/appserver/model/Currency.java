/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.appserver.model;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;

/**
 *
 * @author Jonas
 */
public class Currency implements CurrencyDTO, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name, abbr;
    private float rate;

    public Currency(){
    }
    
    public Currency(String name, String abbr, float rate){
        this.name = name;
        this.abbr = abbr;
        this.rate = rate;
    }
    
    @Override
    public String getName(){
        return name;
    }
    @Override
    public String getAbbr(){
        return abbr;
    }
    @Override
    public float getRate(){
        return rate;
    }
}
